package uk.me.ajv.energykafka;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyAppConfiguration extends Configuration {

    @Valid
    @NotNull
    private KafkaConfiguration kafka = new KafkaConfiguration();

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

}
