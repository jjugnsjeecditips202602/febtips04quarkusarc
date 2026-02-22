package io.github.futokiyo.febtips04quarkusarc;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Path("/rest4quarkusarc")
public class GreetingResource {

    private Aiueo aiueo;

    @Inject
    public GreetingResource(Aiueo aiueo){
        this.aiueo = aiueo;
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        System.out.println(aiueo.irohani());
        Aiueo aiueo2 = CDI.current().select(Aiueo.class).get();
        System.out.println(aiueo2.irohani());

        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/beanlist")
    @Produces(MediaType.TEXT_HTML)
    public String getBeanList() {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beanSet = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>(){});
        Map<String, Integer> counter = new HashMap<>();
        Map<String, List<String>> scopeMap = new HashMap<>();
        for(Bean<?> bean : beanSet) {
            String fqcn = bean.getBeanClass().getCanonicalName();
            String scopeName = bean.getScope().getName().replace("jakarta.enterprise.context.", "");
            if(!counter.containsKey(fqcn)) {
                counter.put(fqcn, 1);
                List<String> list = new ArrayList<>();
                list.add(scopeName);
                scopeMap.put(fqcn, list);
            } else {
                int num = counter.get(fqcn);
                counter.put(fqcn, num+1);

                List<String> list = scopeMap.get(fqcn);
                list.add(scopeName);
                scopeMap.put(fqcn, list);
            }
        }

        StringBuilder returningSb = new StringBuilder("<html><body>");
        counter.keySet().stream().sorted().forEach(clazzNm -> {
            returningSb.append(clazzNm + " size:" + counter.get(clazzNm))
                    .append(" scope:" + String.join(",", scopeMap.get(clazzNm)))
                    .append("<br />");
        });

        returningSb.append("</body></html>");

        return returningSb.toString();
    }


    @GET
    @Path("/memoryusage")
    @Produces(MediaType.TEXT_HTML)
    public String getMemoryUsage() {
        StringBuilder returningSb = new StringBuilder("<html><body><p>");
        returningSb.append(generateMemoryUsage());
        returningSb.append("</p></body></html>");
        return returningSb.toString();
    }

    private String generateMemoryUsage() {

        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        return "total:" + (total/(1024*1024)) + "MB, free:" + (free/(1024*1024)) + "MB, usage:" + ((total - free)/(1024*1024)) + "MB";
    }

}
