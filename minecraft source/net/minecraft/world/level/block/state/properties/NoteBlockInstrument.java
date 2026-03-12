/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public static enum NoteBlockInstrument implements StringRepresentable {
/*    */   private final String name;
/*    */   private final Holder<SoundEvent> soundEvent;
/*  9 */   HARP("harp", SoundEvents.NOTE_BLOCK_HARP, Type.BASE_BLOCK),
/* 10 */   BASEDRUM("basedrum", SoundEvents.NOTE_BLOCK_BASEDRUM, Type.BASE_BLOCK),
/* 11 */   SNARE("snare", SoundEvents.NOTE_BLOCK_SNARE, Type.BASE_BLOCK),
/* 12 */   HAT("hat", SoundEvents.NOTE_BLOCK_HAT, Type.BASE_BLOCK),
/* 13 */   BASS("bass", SoundEvents.NOTE_BLOCK_BASS, Type.BASE_BLOCK),
/* 14 */   FLUTE("flute", SoundEvents.NOTE_BLOCK_FLUTE, Type.BASE_BLOCK),
/* 15 */   BELL("bell", SoundEvents.NOTE_BLOCK_BELL, Type.BASE_BLOCK),
/* 16 */   GUITAR("guitar", SoundEvents.NOTE_BLOCK_GUITAR, Type.BASE_BLOCK),
/* 17 */   CHIME("chime", SoundEvents.NOTE_BLOCK_CHIME, Type.BASE_BLOCK),
/* 18 */   XYLOPHONE("xylophone", SoundEvents.NOTE_BLOCK_XYLOPHONE, Type.BASE_BLOCK),
/* 19 */   IRON_XYLOPHONE("iron_xylophone", SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, Type.BASE_BLOCK),
/* 20 */   COW_BELL("cow_bell", SoundEvents.NOTE_BLOCK_COW_BELL, Type.BASE_BLOCK),
/* 21 */   DIDGERIDOO("didgeridoo", SoundEvents.NOTE_BLOCK_DIDGERIDOO, Type.BASE_BLOCK),
/* 22 */   BIT("bit", SoundEvents.NOTE_BLOCK_BIT, Type.BASE_BLOCK),
/* 23 */   BANJO("banjo", SoundEvents.NOTE_BLOCK_BANJO, Type.BASE_BLOCK),
/* 24 */   PLING("pling", SoundEvents.NOTE_BLOCK_PLING, Type.BASE_BLOCK),
/* 25 */   ZOMBIE("zombie", SoundEvents.NOTE_BLOCK_IMITATE_ZOMBIE, Type.MOB_HEAD),
/* 26 */   SKELETON("skeleton", SoundEvents.NOTE_BLOCK_IMITATE_SKELETON, Type.MOB_HEAD),
/* 27 */   CREEPER("creeper", SoundEvents.NOTE_BLOCK_IMITATE_CREEPER, Type.MOB_HEAD),
/* 28 */   DRAGON("dragon", SoundEvents.NOTE_BLOCK_IMITATE_ENDER_DRAGON, Type.MOB_HEAD),
/* 29 */   WITHER_SKELETON("wither_skeleton", SoundEvents.NOTE_BLOCK_IMITATE_WITHER_SKELETON, Type.MOB_HEAD),
/* 30 */   PIGLIN("piglin", SoundEvents.NOTE_BLOCK_IMITATE_PIGLIN, Type.MOB_HEAD),
/* 31 */   CUSTOM_HEAD("custom_head", SoundEvents.UI_BUTTON_CLICK, Type.CUSTOM);
/*    */   private final Type type;
/*    */   
/*    */   private enum Type {
/* 35 */     BASE_BLOCK,
/* 36 */     MOB_HEAD,
/* 37 */     CUSTOM;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   NoteBlockInstrument(String name, Holder<SoundEvent> soundEvent, Type type) {
/* 45 */     this.name = name;
/* 46 */     this.soundEvent = soundEvent;
/* 47 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Holder<SoundEvent> getSoundEvent() { return this.soundEvent; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean isTunable() { return (this.type == Type.BASE_BLOCK); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public boolean hasCustomSound() { return (this.type == Type.CUSTOM); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public boolean worksAboveNoteBlock() { return (this.type != Type.BASE_BLOCK); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\NoteBlockInstrument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */