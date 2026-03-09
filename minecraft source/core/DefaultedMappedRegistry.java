/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class DefaultedMappedRegistry<T>
/*    */   extends MappedRegistry<T>
/*    */   implements DefaultedRegistry<T> {
/*    */   private final Identifier defaultKey;
/*    */   private Holder.Reference<T> defaultValue;
/*    */   
/*    */   public DefaultedMappedRegistry(String defaultKey, ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusiveHolders) {
/* 16 */     super(key, lifecycle, intrusiveHolders);
/* 17 */     this.defaultKey = Identifier.parse(defaultKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder.Reference<T> register(ResourceKey<T> key, T value, RegistrationInfo registrationInfo) {
/* 22 */     Holder.Reference<T> result = super.register(key, value, registrationInfo);
/* 23 */     if (this.defaultKey.equals(key.identifier())) {
/* 24 */       this.defaultValue = result;
/*    */     }
/* 26 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(T thing) {
/* 31 */     int id = super.getId(thing);
/* 32 */     return (id == -1) ? super.getId(this.defaultValue.value()) : id;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getKey(T thing) {
/* 37 */     Identifier k = super.getKey(thing);
/* 38 */     return (k == null) ? this.defaultKey : k;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getValue(Identifier key) {
/* 43 */     T t = (T)super.getValue(key);
/* 44 */     return (T)((t == null) ? this.defaultValue.value() : t);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public Optional<T> getOptional(Identifier key) { return Optional.ofNullable(super.getValue(key)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Optional<Holder.Reference<T>> getAny() { return Optional.ofNullable(this.defaultValue); }
/*    */ 
/*    */ 
/*    */   
/*    */   public T byId(int id) {
/* 59 */     T t = (T)super.byId(id);
/* 60 */     return (T)((t == null) ? this.defaultValue.value() : t);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Optional<Holder.Reference<T>> getRandom(RandomSource random) { return super.getRandom(random).or(() -> Optional.of(this.defaultValue)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public Identifier getDefaultKey() { return this.defaultKey; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\DefaultedMappedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */