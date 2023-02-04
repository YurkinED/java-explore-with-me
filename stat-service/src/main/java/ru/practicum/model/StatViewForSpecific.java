package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class StatViewForSpecific {
    private String start;
    private String end;
    private String[] uris;
    private boolean unique;


    @Override
    public String toString() {
        return "StatViewForSpecific{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", uris=" + Arrays.toString(uris) +
                ", unique=" + unique +
                '}';
    }
}
