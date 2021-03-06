package noria.swing.components

import noria.*
import java.util.*
import kotlin.reflect.*

class BeanHostProps<T: Any> : HostProps() {
    fun <V> set(settter: KFunction2<T, V, Unit>, value: V) {
        valuesMap[settter.name.removePrefix("set").decapitalize()] = value
    }

    fun <V: EventListener> listen(settter: KFunction2<T, V, Unit>, value: V) {
        valuesMap[settter.name.removePrefix("add").decapitalize()] = value
    }
}

fun <T: Any> beanHostCompnentType(klass: KClass<T>) = HostComponentType<BeanHostProps<T>>(klass.qualifiedName!!)

class ManagedBeanView<B:Any, Props>(val type: HostComponentType<BeanHostProps<B>>, val build: BeanHostProps<B>.(Props) -> Unit) : View<Props>() {
    override fun RenderContext.render() {
        val bhp = BeanHostProps<B>().apply {
            build(props)
        }

        x(type, bhp)
    }
}

fun <B: Any, Props> beanView(klass: KClass<B>, build: BeanHostProps<B>.(Props) -> Unit) = ManagedBeanView(beanHostCompnentType(klass), build)
