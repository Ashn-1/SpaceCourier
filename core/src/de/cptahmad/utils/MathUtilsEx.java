package de.cptahmad.utils;

/**
 * Created by ahmad on 11.09.17.
 */
public class MathUtilsEx
{
    /**
     * Returns the value of a linear function with the given gradient and offset at the given point x.
     * f(x) = gradient * x + offset
     *
     * @param gradient
     * @param offset
     * @param x
     * @return
     */
    public static float linearFunc(float gradient, float offset, float x)
    {
        return (gradient * x + offset);
    }
}
