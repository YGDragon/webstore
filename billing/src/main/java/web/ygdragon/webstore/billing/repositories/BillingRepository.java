package web.ygdragon.webstore.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.ygdragon.webstore.billing.models.Bill;

@Repository
public interface BillingRepository extends JpaRepository<Bill, Long> {
    Bill findByNumber(Long number);
}