/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public static enum PackCompatibility {
/*  9 */   TOO_OLD("old"),
/* 10 */   TOO_NEW("new"),
/* 11 */   UNKNOWN("unknown"),
/* 12 */   COMPATIBLE("compatible");
/*    */   
/*    */   public static final int UNKNOWN_VERSION = 2147483647;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final Component confirmation;
/*    */   
/*    */   PackCompatibility(String key) {
/* 21 */     this.description = Component.translatable("pack.incompatible." + key).withStyle(ChatFormatting.GRAY);
/* 22 */     this.confirmation = Component.translatable("pack.incompatible.confirm." + key);
/*    */   }
/*    */ 
/*    */   
/* 26 */   public boolean isCompatible() { return (this == COMPATIBLE); }
/*    */ 
/*    */   
/*    */   public static PackCompatibility forVersion(InclusiveRange<PackFormat> packDeclaredVersions, PackFormat gameSupportedVersion) {
/* 30 */     if (((PackFormat)packDeclaredVersions.minInclusive()).major() == Integer.MAX_VALUE) {
/* 31 */       return UNKNOWN;
/*    */     }
/* 33 */     if (((PackFormat)packDeclaredVersions.maxInclusive()).compareTo(gameSupportedVersion) < 0) {
/* 34 */       return TOO_OLD;
/*    */     }
/* 36 */     if (gameSupportedVersion.compareTo((PackFormat)packDeclaredVersions.minInclusive()) < 0) {
/* 37 */       return TOO_NEW;
/*    */     }
/* 39 */     return COMPATIBLE;
/*    */   }
/*    */ 
/*    */   
/* 43 */   public Component getDescription() { return this.description; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Component getConfirmation() { return this.confirmation; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\PackCompatibility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */