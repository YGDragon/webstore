package web.ygdragon.webstore.shopping.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import web.ygdragon.webstore.shopping.models.Product;
import web.ygdragon.webstore.shopping.services.ShoppingAdapter;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ShoppingController {
//    private final ShoppingService shoppingService;
    private final ShoppingAdapter shoppingAdapter;
    /**
     * Cart page
     *
     * @param model Data representation model
     * @return cart.html
     */
    @GetMapping("/")
    public String cartPage(
            Model model,
            @RequestParam(value = "confirm", required = false) String confirm) {

        model.addAttribute("products", shoppingAdapter.getAllProducts());
        if (confirm != null) {
            model.addAttribute("confirm", confirm);
        }
        return "cart";
    }

    /**
     * Error page
     *
     * @param exception Exception object
     * @param model     Data representation model
     * @return error.html
     */
    @ExceptionHandler(RuntimeException.class)
    public String errorPage(Principal principal, RuntimeException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("products", shoppingAdapter.getAllProducts());
        return "cart";
    }

    /**
     * Buying product
     *
     * @param idProduct     Product ID
     * @param quantityOrder Quantity product in order
     * @return Redirect to cart.html
     */
    @PostMapping("/buy/{id}")
    public String buy(
            Principal principal,
            @PathVariable("id") Long idProduct,
            @RequestParam("quantity") Integer quantityOrder,
            RedirectAttributes redirectAttributes) {

        Optional<Product> product = shoppingAdapter.getAllProducts().stream()
                .filter(p -> p.id().equals(idProduct))
                .findFirst();

            BigDecimal totalPrice = product.get().price().multiply(
                    new BigDecimal(quantityOrder)
            );
            shoppingAdapter.paymentOrder(
                    product.get().id(),
                    quantityOrder,
                    totalPrice,
                    1L);
            redirectAttributes.addAttribute(
                    "confirm", "Buying successful!");
            return "redirect:/";
    }
}
