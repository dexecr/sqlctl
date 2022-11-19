package com.dexecr.nativeimage;


import com.dexecr.sql.DriverType;
import com.oracle.svm.core.configure.ResourcesRegistry;
import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.graalvm.nativeimage.impl.ConfigurationCondition;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class JdbcFeature implements Feature {

    private static final int CONSTRUCTOR = 1;
    private static final int METHODS = 1 << 1;
    private static final int FIELDS = 1 << 2;
    private static final int INSTANTIATION = 1 << 3;

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        mysql(access);
        postgresql(access);
    }

    private void mysql(BeforeAnalysisAccess access) {
        Arrays.asList(
                "com.mysql.cj.exceptions.AssertionFailedException",
                "com.mysql.cj.exceptions.CJCommunicationsException",
                "com.mysql.cj.exceptions.CJConnectionFeatureNotAvailableException",
                "com.mysql.cj.exceptions.CJException",
                "com.mysql.cj.exceptions.CJOperationNotSupportedException",
                "com.mysql.cj.exceptions.CJPacketTooBigException",
                "com.mysql.cj.exceptions.CJTimeoutException",
                "com.mysql.cj.exceptions.ClosedOnExpiredPasswordException",
                "com.mysql.cj.exceptions.ConnectionIsClosedException",
                "com.mysql.cj.exceptions.DataConversionException",
                "com.mysql.cj.exceptions.DataReadException",
                "com.mysql.cj.exceptions.DataTruncationException",
                "com.mysql.cj.exceptions.FeatureNotAvailableException",
                "com.mysql.cj.exceptions.InvalidConnectionAttributeException",
                "com.mysql.cj.exceptions.MysqlErrorNumbers",
                "com.mysql.cj.exceptions.NumberOutOfRange",
                "com.mysql.cj.exceptions.OperationCancelledException",
                "com.mysql.cj.exceptions.PasswordExpiredException",
                "com.mysql.cj.exceptions.PropertyNotModifiableException",
                "com.mysql.cj.exceptions.RSAException",
                "com.mysql.cj.exceptions.SSLParamsException",
                "com.mysql.cj.exceptions.StatementIsClosedException",
                "com.mysql.cj.exceptions.UnableToConnectException",
                "com.mysql.cj.exceptions.UnsupportedConnectionStringException",
                "com.mysql.cj.exceptions.WrongArgumentException"
        ).forEach(name -> register(access, name, CONSTRUCTOR));

        register(access, DriverType.mysql.driverClassName, FIELDS | METHODS | INSTANTIATION);
        register(access, "com.mysql.cj.log.StandardLogger", CONSTRUCTOR | METHODS | FIELDS);
        register(access, "com.mysql.cj.conf.url.SingleConnectionUrl", CONSTRUCTOR | METHODS | FIELDS);

        register(access, "com.mysql.cj.conf.url.XDevApiConnectionUrl", CONSTRUCTOR | METHODS | FIELDS);
        register(access, "com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream", CONSTRUCTOR | METHODS | FIELDS);
        register(access, "java.util.zip.InflaterInputStream", CONSTRUCTOR | METHODS | FIELDS);

        register(access, "com.mysql.cj.protocol.StandardSocketFactory", FIELDS | METHODS | INSTANTIATION);
        register(access, "com.mysql.cj.jdbc.AbandonedConnectionCleanupThread", FIELDS | METHODS | INSTANTIATION);

        addResource(ResourceType.RESOURCE,
                "com/mysql/cj/TlsSettings.properties",
                "com/mysql/cj/LocalizedErrorMessages.properties",
                "com/mysql/cj/util/TimeZoneMapping.properties"
        );
        addResource(ResourceType.BUNDLE,"com.mysql.cj.LocalizedErrorMessages");
    }

    private void postgresql(BeforeAnalysisAccess access) {
        register(access, "org.postgresql.PGProperty", CONSTRUCTOR | METHODS | FIELDS);
    }

    private static void register(BeforeAnalysisAccess access, String name, int options) {
        var cls = access.findClassByName(name);
        if (cls != null) {
            RuntimeReflection.register(cls);
            if ((options & CONSTRUCTOR) == CONSTRUCTOR) {
                for (Constructor<?> constructor : cls.getConstructors()) {
                    RuntimeReflection.register(constructor);
                }
            }
            if ((options & METHODS) == METHODS) {
                for (var method : cls.getMethods()) {
                    RuntimeReflection.register(method);
                }
            }
            if ((options & FIELDS) == FIELDS) {
                for (var field : cls.getFields()) {
                    RuntimeReflection.register(field);
                }
            }
            if ((options & INSTANTIATION) == INSTANTIATION) {
                RuntimeReflection.registerForReflectiveInstantiation(cls);
            }
        }
    }

    public static void addResource(ResourceType type, String... resources) {
        ResourcesRegistry resourcesRegistry = ImageSingletons.lookup(ResourcesRegistry.class);
        if (resourcesRegistry != null) {
            BiConsumer<ConfigurationCondition, String> resourceConsumer = switch (type) {
                case RESOURCE -> resourcesRegistry::addResources;
                case BUNDLE ->  resourcesRegistry::addResourceBundles;
            };
            for (String resource : resources) {
                resourceConsumer.accept(ConfigurationCondition.alwaysTrue(), resource);
            }
        }
    }

    private enum ResourceType {
        RESOURCE, BUNDLE
    }


}
