package web.ygdragon.webstore.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.ygdragon.webstore.storage.models.Product;

@Repository
public interface StorageRepository extends JpaRepository<Product, Long> {
}