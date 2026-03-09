/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum AdvancementType implements StringRepresentable {
/* 11 */   TASK("task", ChatFormatting.GREEN),
/* 12 */   CHALLENGE("challenge", ChatFormatting.DARK_PURPLE),
/* 13 */   GOAL("goal", ChatFormatting.GREEN); public static final Codec<AdvancementType> CODEC;
/*    */   
/*    */   static  {
/* 16 */     CODEC = StringRepresentable.fromEnum(AdvancementType::values);
/*    */   }
/*    */   private final String name;
/*    */   private final ChatFormatting chatColor;
/*    */   private final Component displayName;
/*    */   
/*    */   AdvancementType(String name, ChatFormatting chatColor) {
/* 23 */     this.name = name;
/* 24 */     this.chatColor = chatColor;
/* 25 */     this.displayName = Component.translatable("advancements.toast." + name);
/*    */   }
/*    */ 
/*    */   
/* 29 */   public ChatFormatting getChatColor() { return this.chatColor; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Component getDisplayName() { return this.displayName; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public MutableComponent createAnnouncement(AdvancementHolder holder, ServerPlayer player) { return Component.translatable("chat.type.advancement." + this.name, new Object[] { player.getDisplayName(), Advancement.name(holder) }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\AdvancementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */