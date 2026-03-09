/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*     */ import net.minecraft.server.packs.OverlayMetadataSection;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class Pack
/*     */ {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger(); private final PackLocationInfo location; private final ResourcesSupplier resources; private final Metadata metadata;
/*     */   private final PackSelectionConfig selectionConfig;
/*     */   
/*     */   public static interface ResourcesSupplier {
/*     */     PackResources openPrimary(PackLocationInfo param1PackLocationInfo);
/*     */     
/*     */     PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata); }
/*     */   
/*     */   public static final class Metadata extends Record { private final Component description;
/*     */     private final PackCompatibility compatibility;
/*     */     private final FeatureFlagSet requestedFeatures;
/*     */     private final List<String> overlays;
/*     */     
/*  35 */     public Metadata(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays) { this.description = description; this.compatibility = compatibility; this.requestedFeatures = requestedFeatures; this.overlays = overlays; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/Pack$Metadata;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #35	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*  35 */       //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public PackCompatibility compatibility() { return this.compatibility; } public FeatureFlagSet requestedFeatures() { return this.requestedFeatures; } public List<String> overlays() { return this.overlays; } }
/*     */ 
/*     */   
/*     */   public static Pack readMetaAndCreate(PackLocationInfo location, ResourcesSupplier resources, PackType packType, PackSelectionConfig selectionConfig) {
/*  39 */     PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(packType);
/*  40 */     Metadata meta = readPackMetadata(location, resources, currentPackVersion, packType);
/*  41 */     return (meta != null) ? new Pack(location, resources, meta, selectionConfig) : null;
/*     */   }
/*     */   
/*     */   public Pack(PackLocationInfo location, ResourcesSupplier resources, Metadata metadata, PackSelectionConfig selectionConfig) {
/*  45 */     this.location = location;
/*  46 */     this.resources = resources;
/*  47 */     this.metadata = metadata;
/*  48 */     this.selectionConfig = selectionConfig;
/*     */   }
/*     */   public static Metadata readPackMetadata(PackLocationInfo location, ResourcesSupplier resources, PackFormat currentPackVersion, PackType type) {
/*     */     
/*  52 */     try { PackResources pack = resources.openPrimary(location); 
/*  53 */       try { PackMetadataSection meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.forPackType(type));
/*  54 */         if (meta == null)
/*     */         {
/*  56 */           meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.FALLBACK_TYPE);
/*     */         }
/*  58 */         if (meta == null)
/*  59 */         { LOGGER.warn("Missing metadata in pack {}", location.id());
/*  60 */           Metadata metadata2 = null;
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
/*  72 */           if (pack != null) pack.close();  return metadata2; }  FeatureFlagsMetadataSection featureFlagMeta = (FeatureFlagsMetadataSection)pack.getMetadataSection(FeatureFlagsMetadataSection.TYPE); FeatureFlagSet requiredFlags = (featureFlagMeta != null) ? featureFlagMeta.flags() : FeatureFlagSet.of(); PackCompatibility packCompatibility = PackCompatibility.forVersion(meta.supportedFormats(), currentPackVersion); OverlayMetadataSection overlays = (OverlayMetadataSection)pack.getMetadataSection(OverlayMetadataSection.forPackType(type)); List<String> overlaySet = (overlays != null) ? overlays.overlaysForVersion(currentPackVersion) : List.of(); Metadata metadata1 = new Metadata(meta.description(), packCompatibility, requiredFlags, overlaySet); if (pack != null) pack.close();  return metadata1; } catch (Throwable throwable) { if (pack != null) try { pack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  73 */     { LOGGER.warn("Failed to read pack {} metadata", location.id(), e);
/*     */       
/*  75 */       return null; }
/*     */   
/*     */   }
/*     */   
/*  79 */   public PackLocationInfo location() { return this.location; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Component getTitle() { return this.location.title(); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public Component getDescription() { return this.metadata.description(); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Component getChatLink(boolean enabled) { return this.location.createChatLink(enabled, this.metadata.description); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public PackCompatibility getCompatibility() { return this.metadata.compatibility(); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public FeatureFlagSet getRequestedFeatures() { return this.metadata.requestedFeatures(); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public PackResources open() { return this.resources.openFull(this.location, this.metadata); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public String getId() { return this.location.id(); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public PackSelectionConfig selectionConfig() { return this.selectionConfig; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean isRequired() { return this.selectionConfig.required(); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean isFixedPosition() { return this.selectionConfig.fixedPosition(); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public Position getDefaultPosition() { return this.selectionConfig.defaultPosition(); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public PackSource getPackSource() { return this.location.source(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 132 */     if (this == o) {
/* 133 */       return true;
/*     */     }
/* 135 */     if (!(o instanceof Pack)) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     Pack that = (Pack)o;
/*     */     
/* 141 */     return this.location.equals(that.location);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public int hashCode() { return this.location.hashCode(); }
/*     */   
/*     */   public enum Position
/*     */   {
/* 150 */     TOP,
/* 151 */     BOTTOM;
/*     */ 
/*     */     
/*     */     public <T> int insert(List<T> list, T value, Function<T, PackSelectionConfig> converter, boolean reverse) {
/* 155 */       Position self = reverse ? opposite() : this;
/* 156 */       if (self == BOTTOM) {
/* 157 */         int index = 0;
/* 158 */         while (index < list.size()) {
/* 159 */           PackSelectionConfig pack = (PackSelectionConfig)converter.apply(list.get(index));
/* 160 */           if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 161 */             index++;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 166 */         list.add(index, value);
/* 167 */         return index;
/*     */       } 
/* 169 */       int index = list.size() - 1;
/* 170 */       while (index >= 0) {
/* 171 */         PackSelectionConfig pack = (PackSelectionConfig)converter.apply(list.get(index));
/* 172 */         if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 173 */           index--;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 178 */       list.add(index + 1, value);
/* 179 */       return index + 1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 184 */     public Position opposite() { return (this == TOP) ? BOTTOM : TOP; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\Pack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */