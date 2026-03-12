/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Target
/*     */   implements StringRepresentable
/*     */ {
/*  94 */   CUSTOM_NAME("custom_name"),
/*  95 */   ITEM_NAME("item_name"); public static final Codec<Target> CODEC; private final String name;
/*     */   
/*     */   static  {
/*  98 */     CODEC = StringRepresentable.fromEnum(Target::values);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   Target(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataComponentType<Component> component() {
/* 113 */     switch (ordinal()) { default: throw new MatchException(null, null);case 1: case 0: break; }  return 
/*     */       
/* 115 */       DataComponents.CUSTOM_NAME;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetNameFunction$Target.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */