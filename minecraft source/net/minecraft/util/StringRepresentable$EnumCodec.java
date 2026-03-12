/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnumCodec<E extends Enum<E> & StringRepresentable>
/*    */   extends StringRepresentable.StringRepresentableCodec<E>
/*    */ {
/*    */   private final Function<String, E> resolver;
/*    */   
/*    */   public EnumCodec(E[] valueArray, Function<String, E> nameResolver) {
/* 49 */     super(valueArray, nameResolver, rec$ -> ((Enum)rec$).ordinal());
/* 50 */     this.resolver = nameResolver;
/*    */   }
/*    */ 
/*    */   
/* 54 */   public E byName(String name) { return (E)(Enum)this.resolver.apply(name); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public E byName(String name, E _default) { return (E)(Enum)Objects.requireNonNullElse(byName(name), _default); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public E byName(String name, Supplier<? extends E> defaultSupplier) { return (E)(Enum)Objects.requireNonNullElseGet(byName(name), defaultSupplier); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StringRepresentable$EnumCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */