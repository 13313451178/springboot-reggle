package com.jkh.reggie.Dto;


import com.jkh.reggie.entity.Setmeal;
import com.jkh.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
