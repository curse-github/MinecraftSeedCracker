/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.ChatFormatting;
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
/*     */ public static enum BossBarColor
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Codec<BossBarColor> CODEC;
/*  92 */   PINK("pink", ChatFormatting.RED),
/*  93 */   BLUE("blue", ChatFormatting.BLUE),
/*  94 */   RED("red", ChatFormatting.DARK_RED),
/*  95 */   GREEN("green", ChatFormatting.GREEN),
/*  96 */   YELLOW("yellow", ChatFormatting.YELLOW),
/*  97 */   PURPLE("purple", ChatFormatting.DARK_BLUE),
/*  98 */   WHITE("white", ChatFormatting.WHITE);
/*     */   
/*     */   static  {
/* 101 */     CODEC = StringRepresentable.fromEnum(BossBarColor::values);
/*     */   }
/*     */   
/*     */   private final String name;
/*     */   
/*     */   BossBarColor(String name, ChatFormatting formatting) {
/* 107 */     this.name = name;
/* 108 */     this.formatting = formatting;
/*     */   }
/*     */   private final ChatFormatting formatting;
/*     */   
/* 112 */   public ChatFormatting getFormatting() { return this.formatting; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\BossEvent$BossBarColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */