/*    */ package net.minecraft.server.packs.resources;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.IdentifierPattern;
/*    */ 
/*    */ public class ResourceFilterSection {
/* 11 */   private static final Codec<ResourceFilterSection> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 12 */         Codec.list(IdentifierPattern.CODEC).fieldOf("block").forGetter(()))
/* 13 */       .apply(i, ResourceFilterSection::new));
/*    */   
/* 15 */   public static final MetadataSectionType<ResourceFilterSection> TYPE = new MetadataSectionType("filter", CODEC);
/*    */   
/*    */   private final List<IdentifierPattern> blockList;
/*    */ 
/*    */   
/* 20 */   public ResourceFilterSection(List<IdentifierPattern> blockList) { this.blockList = List.copyOf(blockList); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean isNamespaceFiltered(String namespace) { return this.blockList.stream().anyMatch(p -> p.namespacePredicate().test(namespace)); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isPathFiltered(String path) { return this.blockList.stream().anyMatch(p -> p.pathPredicate().test(path)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ResourceFilterSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */