package com.appp.ecovitae

import java.util.*

class CheckBoxList: Observable() {
    var isSelected: Boolean = false
    var trash: String? = null

    fun getAnimals(): String {
        return this.trash.toString()
    }

    fun setAnimals(animal: String) {
        this.trash = animal
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }

}