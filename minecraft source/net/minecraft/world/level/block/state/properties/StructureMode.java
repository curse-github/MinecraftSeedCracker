/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum StructureMode implements StringRepresentable {
/*    */   @Deprecated
/*    */   public static final Codec<StructureMode> LEGACY_CODEC;
/*  9 */   SAVE("save"),
/* 10 */   LOAD("load"),
/* 11 */   CORNER("corner"),
/* 12 */   DATA("data");
/*    */ 
/*    */   
/*    */   static  {
/* 16 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(StructureMode::valueOf);
/*    */   }
/*    */   private final String name;
/*    */   private final Component displayName;
/*    */   
/*    */   StructureMode(String name) {
/* 22 */     this.name = name;
/* 23 */     this.displayName = Component.translatable("structure_block.mode_info." + name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public Component getDisplayName() { return this.displayName; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\StructureMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */