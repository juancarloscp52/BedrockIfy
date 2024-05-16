package me.juancarloscp52.bedrockify.test.client.features.bedrockShading.sunGlare;

import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockSunGlareShading;
import net.irisshaders.iris.Iris;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Test cases of compatibility with Iris.<br>
 * This information matches to {@link BedrockSunGlareShading#MOD_ID_CLASS_MAP}. If some errors appeared here, it needs to be updated.
 */
public class IrisCompatTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IrisCompatTest.class);

    private static final String IRIS_CLASS_CANONICAL_NAME = Iris.class.getCanonicalName();
    private static final String IRIS_GET_SHADER_PACK_METHOD_NAME = "getCurrentPack";
    private static final String SUN_GLARE_CLASS_NAME = BedrockSunGlareShading.class.getSimpleName();
    private static final String SUN_GLARE_PACKAGE_NAME = BedrockSunGlareShading.class.getPackageName();

    @Test
    public void checkExactClassMethodName() throws Throwable {
        try {
            Class<?> classNameCheck = Class.forName(IRIS_CLASS_CANONICAL_NAME, false, IrisCompatTest.class.getClassLoader());
            Method methodExistsCheck = classNameCheck.getMethod(IRIS_GET_SHADER_PACK_METHOD_NAME);
            // Object invocationCheck = methodExistsCheck.invoke(classNameCheck);  // Cannot invoke due to Iris' static-initializer error
            Assertions.assertNotNull(methodExistsCheck);
        } catch (NoClassDefFoundError ex) {
            if (ex.getMessage().contains("net/minecraft/class_")) {
                LOGGER.error(
                        """
                        #!@!# Non-named Minecraft class definition is present!
                        Make sure you are using "Fabric Loom 1.0" or above, and import the Iris by "modTestImplementation" in build.gradle.
                        """, ex);
            } else {
                throw ex;
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.error("#!@!# The class cannot retrieve: \"{}\".\nPlease check Canonical Name of Iris, and update \"{}\" in package \"{}\".\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    SUN_GLARE_CLASS_NAME,
                    SUN_GLARE_PACKAGE_NAME);
            throw ex;
        } catch (NoSuchMethodException ex) {
            LOGGER.error("#!@!# The method cannot retrieve: \"{}::{}\".\nPlease check Method Name in Iris, and update \"{}\" in package \"{}\".\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME,
                    SUN_GLARE_CLASS_NAME,
                    SUN_GLARE_PACKAGE_NAME);
            throw ex;
        } catch (SecurityException ex) {
            LOGGER.error("#!@!# The access modifier of the method has changed: \"{}::{}\".\nPlease check the Method in Iris, and update \"{}\" in package \"{}\".\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME,
                    SUN_GLARE_CLASS_NAME,
                    SUN_GLARE_PACKAGE_NAME);
            throw ex;
        } catch (IllegalArgumentException ex) {
            LOGGER.error("#!@!# The acceptable arguments of the method has changed: \"{}::{}\".\nPlease check the Method in Iris, and update \"{}\" in package \"{}\".\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME,
                    SUN_GLARE_CLASS_NAME,
                    SUN_GLARE_PACKAGE_NAME);
            throw ex;
        } catch (ClassCastException ex) {
            LOGGER.error("#!@!# The return type of the method has changed: \"{}::{}\".\nPlease check the Method in Iris, and update \"{}\" in package \"{}\".\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME,
                    SUN_GLARE_CLASS_NAME,
                    SUN_GLARE_PACKAGE_NAME);
            throw ex;
        }
    }
}
