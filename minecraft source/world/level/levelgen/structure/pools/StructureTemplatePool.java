/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class StructureTemplatePool {
/*     */   private static final int SIZE_UNSET = -2147483648;
/*  30 */   private static final MutableObject<Codec<Holder<StructureTemplatePool>>> CODEC_REFERENCE = new MutableObject(); public static final Codec<Holder<StructureTemplatePool>> CODEC; private final List<Pair<StructurePoolElement, Integer>> rawTemplates;
/*     */   private final ObjectArrayList<StructurePoolElement> templates;
/*  32 */   public static final Codec<StructureTemplatePool> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
/*  33 */         Codec.lazyInitialized(CODEC_REFERENCE).fieldOf("fallback").forGetter(StructureTemplatePool::getFallback), 
/*  34 */         Codec.mapPair(StructurePoolElement.CODEC
/*  35 */           .fieldOf("element"), 
/*     */           
/*  37 */           Codec.intRange(1, 150).fieldOf("weight"))
/*  38 */         .codec().listOf().fieldOf("elements").forGetter(()))
/*  39 */       .apply(i, StructureTemplatePool::new)); private final Holder<StructureTemplatePool> fallback;
/*     */   static  {
/*  41 */     Objects.requireNonNull(CODEC_REFERENCE); CODEC = (Codec)Util.make(RegistryFileCodec.create(Registries.TEMPLATE_POOL, DIRECT_CODEC), CODEC_REFERENCE::setValue);
/*     */   }
/*     */   private int maxSize;
/*  44 */   public enum Projection implements StringRepresentable { TERRAIN_MATCHING("terrain_matching", 
/*     */       
/*  46 */       ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))),
/*     */     
/*  48 */     RIGID("rigid", 
/*     */       
/*  50 */       ImmutableList.of()); public static final StringRepresentable.EnumCodec<Projection> CODEC; private final String name; private final ImmutableList<StructureProcessor> processors;
/*     */     
/*     */     static  {
/*  53 */       CODEC = StringRepresentable.fromEnum(Projection::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Projection(String name, ImmutableList<StructureProcessor> processors) {
/*  59 */       this.name = name;
/*  60 */       this.processors = processors;
/*     */     }
/*     */ 
/*     */     
/*  64 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/*  68 */     public static Projection byName(String name) { return (Projection)CODEC.byName(name); }
/*     */ 
/*     */ 
/*     */     
/*  72 */     public ImmutableList<StructureProcessor> getProcessors() { return this.processors; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     public String getSerializedName() { return this.name; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureTemplatePool(Holder<StructureTemplatePool> fallback, List<Pair<StructurePoolElement, Integer>> templates) {
/*  84 */     this.maxSize = Integer.MIN_VALUE;
/*     */ 
/*     */     
/*  87 */     this.rawTemplates = templates;
/*  88 */     this.templates = new ObjectArrayList();
/*  89 */     for (Pair<StructurePoolElement, Integer> templateDef : templates) {
/*  90 */       StructurePoolElement element = (StructurePoolElement)templateDef.getFirst();
/*  91 */       for (int i = 0; i < ((Integer)templateDef.getSecond()).intValue(); i++) {
/*  92 */         this.templates.add(element);
/*     */       }
/*     */     } 
/*     */     
/*  96 */     this.fallback = fallback;
/*     */   }
/*     */   public StructureTemplatePool(Holder<StructureTemplatePool> fallback, List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> templates, Projection projection) {
/*     */     this.maxSize = Integer.MIN_VALUE;
/* 100 */     this.rawTemplates = Lists.newArrayList();
/* 101 */     this.templates = new ObjectArrayList();
/* 102 */     for (Pair<Function<Projection, ? extends StructurePoolElement>, Integer> templateDef : templates) {
/* 103 */       StructurePoolElement element = (StructurePoolElement)((Function)templateDef.getFirst()).apply(projection);
/* 104 */       this.rawTemplates.add(Pair.of(element, (Integer)templateDef.getSecond()));
/* 105 */       for (int i = 0; i < ((Integer)templateDef.getSecond()).intValue(); i++) {
/* 106 */         this.templates.add(element);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     this.fallback = fallback;
/*     */   }
/*     */   
/*     */   public int getMaxSize(StructureTemplateManager manager) {
/* 114 */     if (this.maxSize == Integer.MIN_VALUE) {
/* 115 */       this
/*     */ 
/*     */ 
/*     */         
/* 119 */         .maxSize = this.templates.stream().filter(t -> (t != EmptyPoolElement.INSTANCE)).mapToInt(t -> t.getBoundingBox(manager, BlockPos.ZERO, Rotation.NONE).getYSpan()).max().orElse(0);
/*     */     }
/* 121 */     return this.maxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 126 */   public List<Pair<StructurePoolElement, Integer>> getTemplates() { return this.rawTemplates; }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public Holder<StructureTemplatePool> getFallback() { return this.fallback; }
/*     */ 
/*     */   
/*     */   public StructurePoolElement getRandomTemplate(RandomSource random) {
/* 134 */     if (this.templates.isEmpty()) {
/* 135 */       return EmptyPoolElement.INSTANCE;
/*     */     }
/* 137 */     return (StructurePoolElement)this.templates.get(random.nextInt(this.templates.size()));
/*     */   }
/*     */ 
/*     */   
/* 141 */   public List<StructurePoolElement> getShuffledTemplates(RandomSource random) { return Util.shuffledCopy(this.templates, random); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public int size() { return this.templates.size(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\StructureTemplatePool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */