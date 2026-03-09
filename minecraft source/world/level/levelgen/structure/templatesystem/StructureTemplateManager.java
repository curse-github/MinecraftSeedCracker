/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.gametest.framework.StructureUtils;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureTemplateManager {
/*  53 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String STRUCTURE_RESOURCE_DIRECTORY_NAME = "structure";
/*     */   
/*     */   private static final String STRUCTURE_GENERATED_DIRECTORY_NAME = "structures";
/*     */   
/*     */   private static final String STRUCTURE_FILE_EXTENSION = ".nbt";
/*     */   
/*     */   private static final String STRUCTURE_TEXT_FILE_EXTENSION = ".snbt";
/*     */   private final Map<Identifier, Optional<StructureTemplate>> structureRepository;
/*     */   private final DataFixer fixerUpper;
/*     */   private ResourceManager resourceManager;
/*     */   private final Path generatedDir;
/*     */   private final List<Source> sources;
/*     */   private final HolderGetter<Block> blockLookup;
/*  68 */   private static final FileToIdConverter RESOURCE_LISTER = new FileToIdConverter("structure", ".nbt");
/*     */   private static final class Source extends Record { private final Function<Identifier, Optional<StructureTemplate>> loader; private final Supplier<Stream<Identifier>> lister;
/*  70 */     private Source(Function<Identifier, Optional<StructureTemplate>> loader, Supplier<Stream<Identifier>> lister) { this.loader = loader; this.lister = lister; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager$Source;
/*  70 */       //   0	8	1	o	Ljava/lang/Object; } public Function<Identifier, Optional<StructureTemplate>> loader() { return this.loader; } public Supplier<Stream<Identifier>> lister() { return this.lister; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureTemplateManager(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess storage, DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
/*     */     this.structureRepository = Maps.newConcurrentMap();
/*  77 */     this.resourceManager = resourceManager;
/*  78 */     this.fixerUpper = fixerUpper;
/*  79 */     this.generatedDir = storage.getLevelPath(LevelResource.GENERATED_DIR).normalize();
/*  80 */     this.blockLookup = blockLookup;
/*  81 */     ImmutableList.Builder<Source> builder = ImmutableList.builder();
/*  82 */     builder.add(new Source(this::loadFromGenerated, this::listGenerated));
/*  83 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  84 */       builder.add(new Source(this::loadFromTestStructures, this::listTestStructures));
/*     */     }
/*  86 */     builder.add(new Source(this::loadFromResource, this::listResources));
/*  87 */     this.sources = builder.build();
/*     */   }
/*     */   
/*     */   public StructureTemplate getOrCreate(Identifier id) {
/*  91 */     Optional<StructureTemplate> cachedTemplate = get(id);
/*     */     
/*  93 */     if (cachedTemplate.isPresent()) {
/*  94 */       return (StructureTemplate)cachedTemplate.get();
/*     */     }
/*     */     
/*  97 */     StructureTemplate template = new StructureTemplate();
/*  98 */     this.structureRepository.put(id, Optional.of(template));
/*  99 */     return template;
/*     */   }
/*     */ 
/*     */   
/* 103 */   public Optional<StructureTemplate> get(Identifier id) { return (Optional)this.structureRepository.computeIfAbsent(id, this::tryLoad); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Stream<Identifier> listTemplates() { return this.sources.stream().flatMap(s -> (Stream)s.lister().get()).distinct(); }
/*     */ 
/*     */   
/*     */   private Optional<StructureTemplate> tryLoad(Identifier id) {
/* 111 */     for (Source source : this.sources) {
/*     */       try {
/* 113 */         Optional<StructureTemplate> loaded = (Optional)source.loader().apply(id);
/* 114 */         if (loaded.isPresent()) {
/* 115 */           return loaded;
/*     */         }
/* 117 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 120 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 124 */     this.resourceManager = resourceManager;
/* 125 */     this.structureRepository.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<StructureTemplate> loadFromResource(Identifier id) {
/* 134 */     Identifier identifier = RESOURCE_LISTER.idToFile(id);
/* 135 */     return load(() -> this.resourceManager.open(identifier), e -> LOGGER.error("Couldn't load structure {}", id, e));
/*     */   }
/*     */ 
/*     */   
/* 139 */   private Stream<Identifier> listResources() { Objects.requireNonNull(RESOURCE_LISTER); return RESOURCE_LISTER.listMatchingResources(this.resourceManager).keySet().stream().map(RESOURCE_LISTER::fileToId); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   private Optional<StructureTemplate> loadFromTestStructures(Identifier id) { return loadFromSnbt(id, StructureUtils.testStructuresDir); }
/*     */ 
/*     */   
/*     */   private Stream<Identifier> listTestStructures() {
/* 147 */     if (!Files.isDirectory(StructureUtils.testStructuresDir, new java.nio.file.LinkOption[0])) {
/* 148 */       return Stream.empty();
/*     */     }
/*     */     
/* 151 */     List<Identifier> result = new ArrayList<Identifier>();
/* 152 */     Objects.requireNonNull(result); listFolderContents(StructureUtils.testStructuresDir, "minecraft", ".snbt", result::add);
/* 153 */     return result.stream();
/*     */   }
/*     */   
/*     */   private Optional<StructureTemplate> loadFromGenerated(Identifier id) {
/* 157 */     if (!Files.isDirectory(this.generatedDir, new java.nio.file.LinkOption[0])) {
/* 158 */       return Optional.empty();
/*     */     }
/*     */     
/* 161 */     Path file = createAndValidatePathToGeneratedStructure(id, ".nbt");
/* 162 */     return load(() -> new FileInputStream(file.toFile()), e -> LOGGER.error("Couldn't load structure from {}", file, e));
/*     */   }
/*     */   
/*     */   private Stream<Identifier> listGenerated() {
/* 166 */     if (!Files.isDirectory(this.generatedDir, new java.nio.file.LinkOption[0])) {
/* 167 */       return Stream.empty();
/*     */     }
/*     */     
/*     */     try {
/* 171 */       List<Identifier> result = new ArrayList<Identifier>();
/* 172 */       DirectoryStream<Path> contents = Files.newDirectoryStream(this.generatedDir, x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])); 
/* 173 */       try { for (Path namespaceDir : contents) {
/* 174 */           String namespace = namespaceDir.getFileName().toString();
/* 175 */           Path structureDir = namespaceDir.resolve("structures");
/* 176 */           Objects.requireNonNull(result); listFolderContents(structureDir, namespace, ".nbt", result::add);
/*     */         } 
/* 178 */         if (contents != null) contents.close();  } catch (Throwable throwable) { if (contents != null)
/* 179 */           try { contents.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return result.stream();
/* 180 */     } catch (IOException e) {
/* 181 */       return Stream.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void listFolderContents(Path folder, String namespace, String extension, Consumer<Identifier> output) {
/* 186 */     int extensionLength = extension.length();
/* 187 */     Function<String, String> pathProcessor = s -> s.substring(0, s.length() - extensionLength);
/*     */     
/* 189 */     try { Stream<Path> contents = Files.find(folder, 2147483647, (path, attributes) -> 
/*     */           
/* 191 */           (attributes.isRegularFile() && path.toString().endsWith(extension)), new java.nio.file.FileVisitOption[0]); 
/* 192 */       try { contents.forEach(file -> {
/*     */               try {
/* 194 */                 output.accept(Identifier.fromNamespaceAndPath(namespace, (String)pathProcessor.apply(relativize(folder, file))));
/* 195 */               } catch (IdentifierException e) {
/* 196 */                 LOGGER.error("Invalid location while listing folder {} contents", folder, e);
/*     */               } 
/*     */             });
/* 199 */         if (contents != null) contents.close();  } catch (Throwable throwable) { if (contents != null) try { contents.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 200 */     { LOGGER.error("Failed to list folder {} contents", folder, e); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 205 */   private String relativize(Path root, Path file) { return root.relativize(file).toString().replace(File.separator, "/"); }
/*     */ 
/*     */   
/*     */   private Optional<StructureTemplate> loadFromSnbt(Identifier id, Path dir) {
/* 209 */     if (!Files.isDirectory(dir, new java.nio.file.LinkOption[0])) {
/* 210 */       return Optional.empty();
/*     */     }
/*     */     
/* 213 */     Path file = FileUtil.createPathToResource(dir, id.getPath(), ".snbt"); 
/* 214 */     try { BufferedReader reader = Files.newBufferedReader(file); 
/* 215 */       try { String input = IOUtils.toString(reader);
/* 216 */         Optional optional = Optional.of(readStructure(NbtUtils.snbtToStructure(input)));
/* 217 */         if (reader != null) reader.close();  return optional; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (NoSuchFileException e)
/* 218 */     { return Optional.empty(); }
/* 219 */     catch (IOException|com.mojang.brigadier.exceptions.CommandSyntaxException e)
/* 220 */     { LOGGER.error("Couldn't load structure from {}", file, e);
/* 221 */       return Optional.empty(); }
/*     */   
/*     */   }
/*     */   
/*     */   private Optional<StructureTemplate> load(InputStreamOpener opener, Consumer<Throwable> onError) {
/*     */     
/* 227 */     try { InputStream rawInput = opener.open(); 
/* 228 */       try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*     */         
/* 230 */         try { Optional optional = Optional.of(readStructure(fastBufferedInputStream));
/* 231 */           fastBufferedInputStream.close(); if (rawInput != null) rawInput.close();  return optional; } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (rawInput != null) try { rawInput.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (FileNotFoundException e)
/* 232 */     { return Optional.empty(); }
/* 233 */     catch (Throwable e)
/* 234 */     { onError.accept(e);
/* 235 */       return Optional.empty(); }
/*     */   
/*     */   }
/*     */   
/*     */   private StructureTemplate readStructure(InputStream input) throws IOException {
/* 240 */     CompoundTag tag = NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
/* 241 */     return readStructure(tag);
/*     */   }
/*     */   
/*     */   public StructureTemplate readStructure(CompoundTag tag) {
/* 245 */     StructureTemplate structureTemplate = new StructureTemplate();
/*     */     
/* 247 */     int version = NbtUtils.getDataVersion(tag, 500);
/* 248 */     structureTemplate.load(this.blockLookup, DataFixTypes.STRUCTURE.updateToCurrentVersion(this.fixerUpper, tag, version));
/* 249 */     return structureTemplate;
/*     */   }
/*     */   
/*     */   public boolean save(Identifier id) {
/* 253 */     Optional<StructureTemplate> maybeStructureTemplate = (Optional)this.structureRepository.get(id);
/* 254 */     if (maybeStructureTemplate.isEmpty()) {
/* 255 */       return false;
/*     */     }
/*     */     
/* 258 */     StructureTemplate structureTemplate = (StructureTemplate)maybeStructureTemplate.get();
/*     */     
/* 260 */     Path file = createAndValidatePathToGeneratedStructure(id, SharedConstants.DEBUG_SAVE_STRUCTURES_AS_SNBT ? ".snbt" : ".nbt");
/*     */     
/* 262 */     Path parent = file.getParent();
/* 263 */     if (parent == null) {
/* 264 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 268 */       Files.createDirectories(Files.exists(parent, new java.nio.file.LinkOption[0]) ? parent.toRealPath(new java.nio.file.LinkOption[0]) : parent, new java.nio.file.attribute.FileAttribute[0]);
/* 269 */     } catch (IOException e) {
/* 270 */       LOGGER.error("Failed to create parent directory: {}", parent);
/* 271 */       return false;
/*     */     } 
/*     */     
/* 274 */     CompoundTag tag = structureTemplate.save(new CompoundTag());
/* 275 */     if (SharedConstants.DEBUG_SAVE_STRUCTURES_AS_SNBT)
/*     */     { try {
/* 277 */         NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, file, NbtUtils.structureToSnbt(tag));
/* 278 */       } catch (Throwable ignored) {
/* 279 */         return false;
/*     */       }  }
/*     */     else { 
/* 282 */       try { OutputStream output = new FileOutputStream(file.toFile()); 
/* 283 */         try { NbtIo.writeCompressed(tag, output);
/* 284 */           output.close(); } catch (Throwable throwable) { try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable ignored)
/* 285 */       { return false; }
/*     */        }
/*     */     
/* 288 */     return true;
/*     */   }
/*     */   
/*     */   public Path createAndValidatePathToGeneratedStructure(Identifier id, String extension) {
/* 292 */     if (id.getPath().contains("//")) {
/* 293 */       throw new IdentifierException("Invalid resource path: " + String.valueOf(id));
/*     */     }
/*     */     
/*     */     try {
/* 297 */       Path namespaceDir = this.generatedDir.resolve(id.getNamespace());
/* 298 */       Path structureDir = namespaceDir.resolve("structures");
/* 299 */       Path pathToResource = FileUtil.createPathToResource(structureDir, id.getPath(), extension);
/*     */       
/* 301 */       if (!pathToResource.startsWith(this.generatedDir) || !FileUtil.isPathNormalized(pathToResource) || !FileUtil.isPathPortable(pathToResource)) {
/* 302 */         throw new IdentifierException("Invalid resource path: " + String.valueOf(pathToResource));
/*     */       }
/* 304 */       return pathToResource;
/* 305 */     } catch (InvalidPathException e) {
/* 306 */       throw new IdentifierException("Invalid resource path: " + String.valueOf(id), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 311 */   public void remove(Identifier id) { this.structureRepository.remove(id); }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface InputStreamOpener {
/*     */     InputStream open() throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */