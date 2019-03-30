package br.com.cc.service.cep;

import javax.ws.rs.ext.ContextResolver;

import javax.ws.rs.ext.Provider;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;


@Provider
public class GensonProvider implements ContextResolver<Genson> {
    private Genson genson = new GensonBuilder().setSkipNull(true).create();

    public Genson getContext(Class<?> aClass) {
        return genson;
    }
}
