package me.juancarloscp52.bedrockify.test.client.features.bedrockShading.sunGlare;

import net.coderbot.iris.Iris;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Optional;

public class IrisCompatTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IrisCompatTest.class);

    private static final String IRIS_CLASS_CANONICAL_NAME = Iris.class.getCanonicalName();
    private static final String IRIS_GET_SHADER_PACK_METHOD_NAME = "getCurrentPack";

    @Test
    public void checkExactClassMethodName() throws Throwable {
        try {
            Class<?> classNameCheck = Class.forName(IRIS_CLASS_CANONICAL_NAME);
            Method methodExistsCheck = classNameCheck.getMethod(IRIS_GET_SHADER_PACK_METHOD_NAME);
            Object invocationCheck = methodExistsCheck.invoke(classNameCheck);
            Optional<?> castCheck = (Optional<?>) invocationCheck;
            Assertions.assertEquals(castCheck, Iris.getCurrentPack());
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
            LOGGER.error("#!@!# The class cannot retrieve: \"{}\". Please check Canonical Name of Iris.\n",
                    IRIS_CLASS_CANONICAL_NAME);
            throw ex;
        } catch (NoSuchMethodException ex) {
            LOGGER.error("#!@!# The method cannot retrieve: \"{}::{}\". Please check Method Name in Iris.\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME);
            throw ex;
        } catch (SecurityException | IllegalAccessException ex) {
            LOGGER.error("#!@!# The access modifier of the method has changed: \"{}::{}\". Please check the Method in Iris.\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME);
            throw ex;
        } catch (IllegalArgumentException ex) {
            LOGGER.error("#!@!# The acceptable arguments of the method has changed: \"{}::{}\". Please check the Method in Iris.\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME);
            throw ex;
        } catch (ClassCastException ex) {
            LOGGER.error("#!@!# The return type of the method has changed: \"{}::{}\". Please check the Method in Iris.\n",
                    IRIS_CLASS_CANONICAL_NAME,
                    IRIS_GET_SHADER_PACK_METHOD_NAME);
            throw ex;
        }
    }
}
