package web.ygdragon.webstore.shopping.cofig;

import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import web.ygdragon.webstore.shopping.external_api.BillingApi;
import web.ygdragon.webstore.shopping.external_api.StorageApi;
import web.ygdragon.webstore.shopping.services.ShoppingService;

@TestConfiguration
@RequiredArgsConstructor
public class ConfigIntegrationTests {

    @Bean
    @Primary
    public BillingApi paymentClientApi() {
        return Mockito.mock(BillingApi.class);
    }

    @Bean
    @Primary
    public StorageApi storageClientApi() {
        return Mockito.mock(StorageApi.class);
    }

    @Bean
    @Primary
    public ShoppingService shoppingService() {
        return new ShoppingService(paymentClientApi(), storageClientApi(), "3");
    }
}