package lambich.dailyshop.service;

import lambich.dailyshop.beans.Cart;
import lambich.dailyshop.beans.CartItem;
import lambich.dailyshop.beans.Product;
import lambich.dailyshop.beans.User;

import javax.transaction.Transactional;
import java.util.List;

public interface CartService {
    Cart getCartById(Long cartId);

    void saveCart(Cart cart);
    public boolean isProductInCart(Long productId, String username);

    Cart getCartByUser(User user);

    int getItemNumberInCart(User user);

    public CartItem findCartItemByProductAndUser(Product product, User user);

    void removeCartItemFromCart(Product product, User user);

    @Transactional
    void addCart(Cart cart);

    @Transactional
    void addCartItem(CartItem cartItem);

    @Transactional
    void removeCartItem(CartItem cartItem);

    List<CartItem> getCartItemByCartId(Long cartId);

    void clearCart(Cart cart);
}
