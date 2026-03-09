/*    */ package net.minecraft.server.packs.metadata.pack;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public final class PackMetadataSection extends Record {
/*    */   private final Component description;
/*    */   private final InclusiveRange<PackFormat> supportedFormats;
/*    */   
/* 12 */   public PackMetadataSection(Component description, InclusiveRange<PackFormat> supportedFormats) { this.description = description; this.supportedFormats = supportedFormats; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection; } public Component description() { return this.description; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public InclusiveRange<PackFormat> supportedFormats() { return this.supportedFormats; }
/* 13 */   private static final Codec<PackMetadataSection> FALLBACK_CODEC = RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 14 */         .fieldOf("description").forGetter(PackMetadataSection::description))
/* 15 */       .apply(i, ()));
/*    */ 
/*    */   
/* 18 */   private static Codec<PackMetadataSection> codecForPackType(PackType packType) { return RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 19 */           .fieldOf("description").forGetter(PackMetadataSection::description), 
/* 20 */           PackFormat.packCodec(packType).forGetter(PackMetadataSection::supportedFormats))
/* 21 */         .apply(i, PackMetadataSection::new)); }
/*    */ 
/*    */   
/* 24 */   public static final MetadataSectionType<PackMetadataSection> CLIENT_TYPE = new MetadataSectionType("pack", codecForPackType(PackType.CLIENT_RESOURCES));
/* 25 */   public static final MetadataSectionType<PackMetadataSection> SERVER_TYPE = new MetadataSectionType("pack", codecForPackType(PackType.SERVER_DATA));
/* 26 */   public static final MetadataSectionType<PackMetadataSection> FALLBACK_TYPE = new MetadataSectionType("pack", FALLBACK_CODEC);
/*    */   
/*    */   public static MetadataSectionType<PackMetadataSection> forPackType(PackType packType) {
/* 29 */     switch (packType) { default: throw new MatchException(null, null);case CLIENT_RESOURCES: case SERVER_DATA: break; }  return 
/*    */       
/* 31 */       SERVER_TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\metadata\pack\PackMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */