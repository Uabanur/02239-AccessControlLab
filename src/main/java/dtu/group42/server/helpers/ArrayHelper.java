package dtu.group42.server.helpers;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayHelper {
    public static <T,R> List<R> map(T[] source, Function<T, R> mapping){
        return Arrays.stream(source)
        .map(mapping)
        .collect(Collectors.toList());
    }
}
