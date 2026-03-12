/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ 
/*    */ public interface InstrumentTags {
/*  8 */   public static final TagKey<Instrument> REGULAR_GOAT_HORNS = create("regular_goat_horns");
/*  9 */   public static final TagKey<Instrument> SCREAMING_GOAT_HORNS = create("screaming_goat_horns");
/* 10 */   public static final TagKey<Instrument> GOAT_HORNS = create("goat_horns");
/*    */ 
/*    */   
/* 13 */   private static TagKey<Instrument> create(String name) { return TagKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\InstrumentTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */