package org.kinamulen.service;

import org.kinamulen.model.Data;
import org.kinamulen.model.Variant;

import java.util.Map;

public class VariantService {
    public Map<Integer, Variant> getVariant1() {
        return Data.varLauk;
    }
    public Map<Integer, Variant> getVariant2() {
        return Data.varSpicy;
    }
}
