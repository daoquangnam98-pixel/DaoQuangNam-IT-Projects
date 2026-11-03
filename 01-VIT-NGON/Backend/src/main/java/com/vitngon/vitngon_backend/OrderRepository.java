
package com.vitngon.vitngon_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderId(String orderId);

    Optional<Order> findByOrderId(String orderId);
}
