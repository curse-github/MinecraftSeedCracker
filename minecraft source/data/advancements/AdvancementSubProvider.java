/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public interface AdvancementSubProvider
/*    */ {
/*    */   void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer);
/*    */   
/* 14 */   static AdvancementHolder createPlaceholder(String id) { return Advancement.Builder.advancement().build(Identifier.parse(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\advancements\AdvancementSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */