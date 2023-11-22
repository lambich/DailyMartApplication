package lambich.dailyshop.service;

// CartServiceImpl.java
import lambich.dailyshop.beans.Cart;
import lambich.dailyshop.beans.CartItem;
import lambich.dailyshop.beans.Product;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.repository.CartItemRepository;
import lambich.dailyshop.repository.CartRepository;
import lambich.dailyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    @Transactional
    public void saveCart(Cart cart) {
        // Ensure that the cartItems collection is initialized
        if (cart.getCartItems() != null) {
            cart.getCartItems().removeIf(cartItem -> cartItem.getProduct() == null);
        }

        // Use save to save or update the entity
        cartRepository.save(cart);
    }

    public boolean isProductInCart(Long productId, String username) {
        User user = userRepository.findByUsername(username);

        // If the user or the cart is null, the product is not in the cart
        if (user == null || user.getCart() == null) {
            return false;
        }

        // Check if the product with productId is in the user's cart items
        return user.getCart().getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(productId));
    }

    @Override
    @Transactional
    public Cart getCartByUser(User user) {
        Cart cart = cartRepository.findCartByUserId(user.getId());
        // Initialize the cartItems collection here
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().size(); // This will force the initialization
        }
        return cart;
    }

    @Override
    public int getItemNumberInCart(User user) {
        Cart cart = cartRepository.findCartByUserId(user.getId());
        return cart != null ? cart.getCartItems().size() : 0;
    }

    @Override
    public CartItem findCartItemByProductAndUser(Product product, User user) {
        return cartItemRepository.findByProductAndCartUser(product, user);
    }

    @Override
    public void removeCartItemFromCart(Product product, User user) {
        cartItemRepository.deleteByProductAndCartUser(product, user);
    }

    @Override
    @Transactional
    public void addCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItem> getCartItemByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.clearCartItems();
        cartRepository.save(cart);
    }
}
