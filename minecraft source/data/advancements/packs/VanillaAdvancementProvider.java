/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.advancements.AdvancementProvider;
/*    */ 
/*    */ 
/*    */ public class VanillaAdvancementProvider
/*    */ {
/* 12 */   public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) { return new AdvancementProvider(output, registries, 
/*    */ 
/*    */         
/* 15 */         List.of(new VanillaTheEndAdvancements(), new VanillaHusbandryAdvancements(), new VanillaAdventureAdvancements(), new VanillaNetherAdvancements(), new VanillaStoryAdvancements())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\advancements\packs\VanillaAdvancementProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */