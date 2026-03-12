/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ 
/*    */ final class IntermediateEntry
/*    */   extends Record implements PackFormat.IntermediaryFormatHolder {
/*    */   private final PackFormat.IntermediaryFormat format;
/*    */   private final String overlay;
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 18 */   private IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) { this.format = format; this.overlay = overlay; } public PackFormat.IntermediaryFormat format() { return this.format; } public String overlay() { return this.overlay; }
/* 19 */   private static final Codec<IntermediateEntry> CODEC = RecordCodecBuilder.create(i -> i.group(PackFormat.IntermediaryFormat.OVERLAY_CODEC
/* 20 */         .forGetter(IntermediateEntry::format), Codec.STRING
/* 21 */         .validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay))
/* 22 */       .apply(i, IntermediateEntry::new));
/*    */ 
/*    */ 
/*    */   
/* 26 */   public String toString() { return this.overlay; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\OverlayMetadataSection$OverlayEntry$IntermediateEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */