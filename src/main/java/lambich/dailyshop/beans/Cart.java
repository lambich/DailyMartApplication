package lambich.dailyshop.beans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<CartItem> cartItems = new ArrayList<>();
    //@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
    //private Set<CartItem> cartItems;
    //@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    //private Set<CartItem> cartItems = new HashSet<>();
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Cart(Long id, User user, Set<CartItem> cartItems) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
    }

    public Cart() {
        this.cartItems = new HashSet<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal calculateTotalOrder() {
        double totalOrder = 0.0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                totalOrder = totalOrder + item.calculateTotal();
            }
        }
        return BigDecimal.valueOf(totalOrder);
    }

    public void clearCartItems() {
        // Implement the logic to clear cart items
        this.cartItems.clear();
    }
}

