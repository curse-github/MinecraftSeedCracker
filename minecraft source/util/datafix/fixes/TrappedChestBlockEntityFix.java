/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrappedChestBlockEntityFix
/*     */   extends DataFix
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   private static final short SIZE_BITS = 12;
/*     */   
/*  33 */   public TrappedChestBlockEntityFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  38 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/*  39 */     Type<?> levelType = chunkType.findFieldType("Level");
/*  40 */     Type<?> tileEntitiesType = levelType.findFieldType("TileEntities");
/*  41 */     if (!(tileEntitiesType instanceof List.ListType)) {
/*  42 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*     */     }
/*  44 */     List.ListType<?> tileEntityListType = (List.ListType)tileEntitiesType;
/*     */     
/*  46 */     OpticFinder<? extends List<?>> tileEntitiesF = DSL.fieldFinder("TileEntities", tileEntityListType);
/*     */     
/*  48 */     Type<?> chunkType1 = getInputSchema().getType(References.CHUNK);
/*     */     
/*  50 */     OpticFinder<?> levelFinder = chunkType1.findField("Level");
/*  51 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*  52 */     Type<?> sectionsType = sectionsFinder.type();
/*  53 */     if (!(sectionsType instanceof List.ListType)) {
/*  54 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/*  56 */     Type<?> sectionType = ((List.ListType)sectionsType).getElement();
/*  57 */     OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
/*     */     
/*  59 */     return TypeRewriteRule.seq((new AddNewChoices(
/*  60 */           getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY)).makeRule(), 
/*  61 */         fixTypeEverywhereTyped("Trapped Chest fix", chunkType1, chunk -> chunk.updateTyped(levelFinder, ())));
/*     */   }
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
/*     */   public static final class TrappedChestSection
/*     */     extends LeavesFix.Section
/*     */   {
/*     */     private IntSet chestIds;
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
/* 116 */     public TrappedChestSection(Typed<?> section, Schema inputSchema) { super(section, inputSchema); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 121 */       this.chestIds = new IntOpenHashSet();
/*     */       
/* 123 */       for (int i = 0; i < this.palette.size(); i++) {
/* 124 */         Dynamic<?> paletteTag = (Dynamic)this.palette.get(i);
/* 125 */         String blockName = paletteTag.get("Name").asString("");
/* 126 */         if (Objects.equals(blockName, "minecraft:trapped_chest")) {
/* 127 */           this.chestIds.add(i);
/*     */         }
/*     */       } 
/*     */       
/* 131 */       return this.chestIds.isEmpty();
/*     */     }
/*     */ 
/*     */     
/* 135 */     public boolean isTrappedChest(int block) { return this.chestIds.contains(block); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */