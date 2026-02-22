package io.github.futokiyo.febtips04quarkusarc;

import io.github.futokiyo.febtips04quarkusarc.sample.DpndntSampleForApplicationScopedInjection;
import io.github.futokiyo.febtips04quarkusarc.sample.RqstSampleForDynamicLookup;
import io.github.futokiyo.febtips04quarkusarc.sample.RqstSampleForInjection;
import io.github.futokiyo.febtips04quarkusarc.sample.Sample;
import jakarta.enterprise.context.ApplicationScoped;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/rest4quarkusarc")
@ApplicationScoped
public class GreetingResource {

    private static Logger logger = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    private Aiueo aiueo;

    @Inject
    private RqstSampleForInjection rqstSampleForInjection;

    @Inject
    private RqstSampleForDynamicLookup rqstSampleForDynamicLookupByInjection;

    @Inject
    private DpndntSampleForApplicationScopedInjection dpndntSampleForAppInjcton;


    public GreetingResource(){

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
    @Path("/rqst")
    @Produces(MediaType.TEXT_HTML)
    public String getRqst() {
        RqstSampleForDynamicLookup rqstSampleForDynamicLookup = CDI.current().select(RqstSampleForDynamicLookup.class).get();
        StringBuilder returningSb = new StringBuilder("<html><body>");
        returningSb.append("<p>RequestScoped Object (CDI.current().select(RqstSampleForDynamicLookup.class).get()) rqstSampleForDynamicLookup.idUuid:")
                .append(rqstSampleForDynamicLookup.getIdentificationUuid())
                .append("</p><br />");

        if(rqstSampleForDynamicLookup==this.rqstSampleForDynamicLookupByInjection
                && rqstSampleForDynamicLookup.getIdentificationUuid().equals(this.rqstSampleForDynamicLookupByInjection.getIdentificationUuid())
                && rqstSampleForDynamicLookup.getWeight().equals(this.rqstSampleForDynamicLookupByInjection.getWeight())){
            returningSb.append("<p>rqstSampleForDynamicLookup equals rqstSampleForDynamicLookupByInjection</p>");
        }

        returningSb.append("<br />")
                .append(String.format( "<p>rqstSampleForInjection:idUuid:%1$s -> injectedDpndntSampleUuid:%2$s.</p><br />" , rqstSampleForInjection.getIdentificationUuid() , rqstSampleForInjection.getIdUuidOfDpndntSampleForRInjcton() ))
                .append("<p>DpndntSampleForApplicationScopedInjection.idUuid:")
                .append(this.dpndntSampleForAppInjcton.getIdentificationUuid())
                .append("</p><br />");

        returningSb.append(generateMemoryUsage())
                .append("<br />");
        returningSb.append("</body></html>");
        return returningSb.toString();
    }

    @GET
    @Path("/dpndnt")
    @Produces(MediaType.TEXT_HTML)
    public String getDpndnt() {
        Sample sample = CDI.current().select(Sample.class).get();

        StringBuilder returningSb = new StringBuilder("<html><body>");
        returningSb.append("<p>Dependent Object (CDI.current().select(Sample.class).get()) sample.idUuid:")
                .append(sample.getIdentificationUuid())
                .append("</p><br />");



        returningSb.append(generateMemoryUsage())
                .append("<br />");

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
