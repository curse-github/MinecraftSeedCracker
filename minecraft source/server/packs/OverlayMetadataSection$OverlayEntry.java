/*    */ package net.minecraft.server.packs;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public final class OverlayEntry extends Record {
/*    */   private final InclusiveRange<PackFormat> format;
/*    */   private final String overlay;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry; }
/*    */   
/* 17 */   public OverlayEntry(InclusiveRange<PackFormat> format, String overlay) { this.format = format; this.overlay = overlay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public InclusiveRange<PackFormat> format() { return this.format; } public String overlay() { return this.overlay; } private static final class IntermediateEntry extends Record implements PackFormat.IntermediaryFormatHolder { private final PackFormat.IntermediaryFormat format; private final String overlay;
/* 18 */     private IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) { this.format = format; this.overlay = overlay; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 18 */       //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry; } public PackFormat.IntermediaryFormat format() { return this.format; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } public String overlay() { return this.overlay; }
/* 19 */     private static final Codec<IntermediateEntry> CODEC = RecordCodecBuilder.create(i -> i.group(PackFormat.IntermediaryFormat.OVERLAY_CODEC
/* 20 */           .forGetter(IntermediateEntry::format), Codec.STRING
/* 21 */           .validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay))
/* 22 */         .apply(i, IntermediateEntry::new));
/*    */ 
/*    */ 
/*    */     
/* 26 */     public String toString() { return this.overlay; } }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Codec<List<OverlayEntry>> listCodecForPackType(PackType packType) {
/* 31 */     int lastPreMinorVersion = PackFormat.lastPreMinorVersion(packType);
/* 32 */     return IntermediateEntry.CODEC.listOf().flatXmap(list -> 
/* 33 */         PackFormat.validateHolderList(list, lastPreMinorVersion, ()), list -> 
/* 34 */         DataResult.success(list.stream().map(()).toList()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean isApplicable(PackFormat formatToTest) { return this.format.isValueInRange(formatToTest); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\OverlayMetadataSection$OverlayEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */