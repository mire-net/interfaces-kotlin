package com.noxcrew.interfaces.properties

public fun <T> interfaceProperty(value: T): InterfaceProperty<T> = InterfaceProperty(value)

public fun emptyTrigger(): Trigger = EmptyTrigger