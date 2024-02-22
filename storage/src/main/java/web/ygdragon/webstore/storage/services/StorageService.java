package web.ygdragon.webstore.storage.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.ygdragon.webstore.storage.exceptions.ExcessBalanceException;
import web.ygdragon.webstore.storage.exceptions.ProductNotFoundException;
import web.ygdragon.webstore.storage.models.Product;
import web.ygdragon.webstore.storage.repositories.StorageRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StorageService {
    private final StorageRepository storageRepository;

    /**
     * Getting all products from stock
     *
     * @return List of products
     */
    public List<Product> getAllProduct() {
        return storageRepository.findAll();
    }

    /**
     * Getting product from stock by ID
     *
     * @param idProduct Product ID
     * @return Product object by ID
     * @throws ProductNotFoundException An exception if product not finding
     */
    public Product getProductById(Long idProduct) throws ProductNotFoundException {
        return storageRepository.findById(idProduct)
                .orElseThrow(
                        () -> new ProductNotFoundException("INFO >> Product with ID=" + idProduct + " not found!")
                );
    }

    /**
     * Reduce quantity of product from stock by product ID
     *
     * @param idProduct     Product ID
     * @param quantityOrder Quantity of product in order
     * @throws ExcessBalanceException An exception if the quantity exceeds the balance in stock
     */
    @Transactional
    public void reduceProductQuantity(Long idProduct, int quantityOrder) throws ExcessBalanceException {
        Product product = getProductById(idProduct);
        var quantityProduct = product.getQuantity();
        if (quantityOrder > quantityProduct) {
            throw new ExcessBalanceException(
                    "INFO >> The quantity of product exceeds the balance in the stock!");
        }

        product.setQuantity(quantityProduct - quantityOrder);
        var reserveProduct = product.getReserve();
        product.setReserve(reserveProduct - quantityOrder);
        storageRepository.save(product);
    }

    /**
     * Reservation for product in stock by product ID
     *
     * @param idProduct     Product ID
     * @param quantityOrder Quantity of product in order
     * @throws ExcessBalanceException An exception if the quantity exceeds the balance in stock
     */
    @Transactional
    public void reserveProduct(Long idProduct, int quantityOrder) throws ExcessBalanceException {
        Product product = getProductById(idProduct);
        var quantityProduct = product.getQuantity();
        if (quantityOrder > quantityProduct) {
            throw new ExcessBalanceException(
                    "INFO >> The quantity of product exceeds the balance in the stock!");
        }

        product.setReserve(quantityOrder);
        storageRepository.save(product);
    }

    /**
     * Rollback reservation of product in stock by product ID
     *
     * @param idProduct     Product ID
     * @param quantityOrder Quantity of product in order
     */
    @Transactional
    public void rollbackReserveProduct(Long idProduct, int quantityOrder) {
        Product product = getProductById(idProduct);
        product.setReserve(product.getReserve() - quantityOrder);
        storageRepository.save(product);
    }

}