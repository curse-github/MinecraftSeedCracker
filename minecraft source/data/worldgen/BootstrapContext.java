/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface BootstrapContext<T>
/*    */ {
/*    */   Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, Lifecycle paramLifecycle);
/*    */   
/* 13 */   default Holder.Reference<T> register(ResourceKey<T> key, T value) { return register(key, value, Lifecycle.stable()); }
/*    */   
/*    */   <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> paramResourceKey);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\BootstrapContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */