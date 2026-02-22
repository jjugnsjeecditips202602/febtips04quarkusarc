package io.github.futokiyo.febtips04quarkusarc.sample;

import io.github.futokiyo.febtips04quarkusarc.aop.Logging;
import io.github.futokiyo.febtips04quarkusarc.utils.WeightUtils;
import jakarta.enterprise.context.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Dependent
public class DpndntSampleForApplicationScopedInjection {
    private static Logger logger = LoggerFactory.getLogger(DpndntSampleForApplicationScopedInjection.class);

	private final String identificationUuid;
    private final String weight;

    public DpndntSampleForApplicationScopedInjection(){
    	this.identificationUuid = UUID.randomUUID().toString();
        this.weight = WeightUtils.generateWeight(100);
        logger.info("called DpndntSampleForApplicationScopedInjection constructor idUuid:{}", this.identificationUuid);
    }
    @Logging
    public String getIdentificationUuid(){
        return this.identificationUuid;
    }

    @Logging
    public String getWeight(){
        return this.weight;
    }
}
