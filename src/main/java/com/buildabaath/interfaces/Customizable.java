package com.buildabaath.interfaces;

import com.buildabaath.models.abstracts.Topping;
import java.util.List;

public interface Customizable {
    void addTopping(Topping topping);
    void removeTopping(Topping topping);
    List<Topping> getToppings();
}
