/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface Instruments
/*    */ {
/*    */   public static final int GOAT_HORN_RANGE_BLOCKS = 256;
/*    */   public static final float GOAT_HORN_DURATION = 7.0F;
/* 18 */   public static final ResourceKey<Instrument> PONDER_GOAT_HORN = create("ponder_goat_horn");
/* 19 */   public static final ResourceKey<Instrument> SING_GOAT_HORN = create("sing_goat_horn");
/* 20 */   public static final ResourceKey<Instrument> SEEK_GOAT_HORN = create("seek_goat_horn");
/* 21 */   public static final ResourceKey<Instrument> FEEL_GOAT_HORN = create("feel_goat_horn");
/* 22 */   public static final ResourceKey<Instrument> ADMIRE_GOAT_HORN = create("admire_goat_horn");
/* 23 */   public static final ResourceKey<Instrument> CALL_GOAT_HORN = create("call_goat_horn");
/* 24 */   public static final ResourceKey<Instrument> YEARN_GOAT_HORN = create("yearn_goat_horn");
/* 25 */   public static final ResourceKey<Instrument> DREAM_GOAT_HORN = create("dream_goat_horn");
/*    */ 
/*    */   
/* 28 */   private static ResourceKey<Instrument> create(String id) { return ResourceKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */   
/*    */   static void bootstrap(BootstrapContext<Instrument> context) {
/* 32 */     register(context, PONDER_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(0), 7.0F, 256.0F);
/* 33 */     register(context, SING_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(1), 7.0F, 256.0F);
/* 34 */     register(context, SEEK_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2), 7.0F, 256.0F);
/* 35 */     register(context, FEEL_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(3), 7.0F, 256.0F);
/* 36 */     register(context, ADMIRE_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(4), 7.0F, 256.0F);
/* 37 */     register(context, CALL_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(5), 7.0F, 256.0F);
/* 38 */     register(context, YEARN_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(6), 7.0F, 256.0F);
/* 39 */     register(context, DREAM_GOAT_HORN, (Holder)SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(7), 7.0F, 256.0F);
/*    */   }
/*    */   
/*    */   static void register(BootstrapContext<Instrument> context, ResourceKey<Instrument> key, Holder<SoundEvent> soundEvent, float duration, float range) {
/* 43 */     MutableComponent description = Component.translatable(Util.makeDescriptionId("instrument", key.identifier()));
/* 44 */     context.register(key, new Instrument(soundEvent, duration, range, description));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\Instruments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */