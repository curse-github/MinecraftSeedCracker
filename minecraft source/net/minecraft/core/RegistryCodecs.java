/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.resources.HolderSetCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistryCodecs
/*    */ {
/* 11 */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) { return homogeneousList(registryKey, elementCodec, false); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean alwaysUseList) { return HolderSetCodec.create(registryKey, RegistryFileCodec.create(registryKey, elementCodec), alwaysUseList); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey) { return homogeneousList(registryKey, false); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, boolean alwaysUseList) { return HolderSetCodec.create(registryKey, RegistryFixedCodec.create(registryKey), alwaysUseList); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistryCodecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */