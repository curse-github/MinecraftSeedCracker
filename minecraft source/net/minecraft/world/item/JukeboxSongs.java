/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface JukeboxSongs {
/* 14 */   public static final ResourceKey<JukeboxSong> THIRTEEN = create("13");
/* 15 */   public static final ResourceKey<JukeboxSong> CAT = create("cat");
/* 16 */   public static final ResourceKey<JukeboxSong> BLOCKS = create("blocks");
/* 17 */   public static final ResourceKey<JukeboxSong> CHIRP = create("chirp");
/* 18 */   public static final ResourceKey<JukeboxSong> FAR = create("far");
/* 19 */   public static final ResourceKey<JukeboxSong> MALL = create("mall");
/* 20 */   public static final ResourceKey<JukeboxSong> MELLOHI = create("mellohi");
/* 21 */   public static final ResourceKey<JukeboxSong> STAL = create("stal");
/* 22 */   public static final ResourceKey<JukeboxSong> STRAD = create("strad");
/* 23 */   public static final ResourceKey<JukeboxSong> WARD = create("ward");
/* 24 */   public static final ResourceKey<JukeboxSong> ELEVEN = create("11");
/* 25 */   public static final ResourceKey<JukeboxSong> WAIT = create("wait");
/* 26 */   public static final ResourceKey<JukeboxSong> PIGSTEP = create("pigstep");
/* 27 */   public static final ResourceKey<JukeboxSong> OTHERSIDE = create("otherside");
/* 28 */   public static final ResourceKey<JukeboxSong> FIVE = create("5");
/* 29 */   public static final ResourceKey<JukeboxSong> RELIC = create("relic");
/* 30 */   public static final ResourceKey<JukeboxSong> PRECIPICE = create("precipice");
/* 31 */   public static final ResourceKey<JukeboxSong> CREATOR = create("creator");
/* 32 */   public static final ResourceKey<JukeboxSong> CREATOR_MUSIC_BOX = create("creator_music_box");
/* 33 */   public static final ResourceKey<JukeboxSong> TEARS = create("tears");
/* 34 */   public static final ResourceKey<JukeboxSong> LAVA_CHICKEN = create("lava_chicken");
/*    */ 
/*    */   
/* 37 */   private static ResourceKey<JukeboxSong> create(String id) { return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> registryKey, Holder.Reference<SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) { context.register(registryKey, new JukeboxSong(soundEvent, Component.translatable(Util.makeDescriptionId("jukebox_song", registryKey.identifier())), lengthInSeconds, comparatorOutput)); }
/*    */ 
/*    */   
/*    */   static void bootstrap(BootstrapContext<JukeboxSong> context) {
/* 45 */     register(context, THIRTEEN, SoundEvents.MUSIC_DISC_13, 178, 1);
/* 46 */     register(context, CAT, SoundEvents.MUSIC_DISC_CAT, 185, 2);
/* 47 */     register(context, BLOCKS, SoundEvents.MUSIC_DISC_BLOCKS, 345, 3);
/* 48 */     register(context, CHIRP, SoundEvents.MUSIC_DISC_CHIRP, 185, 4);
/* 49 */     register(context, FAR, SoundEvents.MUSIC_DISC_FAR, 174, 5);
/* 50 */     register(context, MALL, SoundEvents.MUSIC_DISC_MALL, 197, 6);
/* 51 */     register(context, MELLOHI, SoundEvents.MUSIC_DISC_MELLOHI, 96, 7);
/* 52 */     register(context, STAL, SoundEvents.MUSIC_DISC_STAL, 150, 8);
/* 53 */     register(context, STRAD, SoundEvents.MUSIC_DISC_STRAD, 188, 9);
/* 54 */     register(context, WARD, SoundEvents.MUSIC_DISC_WARD, 251, 10);
/* 55 */     register(context, ELEVEN, SoundEvents.MUSIC_DISC_11, 71, 11);
/* 56 */     register(context, WAIT, SoundEvents.MUSIC_DISC_WAIT, 238, 12);
/* 57 */     register(context, PIGSTEP, SoundEvents.MUSIC_DISC_PIGSTEP, 149, 13);
/* 58 */     register(context, OTHERSIDE, SoundEvents.MUSIC_DISC_OTHERSIDE, 195, 14);
/* 59 */     register(context, FIVE, SoundEvents.MUSIC_DISC_5, 178, 15);
/* 60 */     register(context, RELIC, SoundEvents.MUSIC_DISC_RELIC, 218, 14);
/* 61 */     register(context, PRECIPICE, SoundEvents.MUSIC_DISC_PRECIPICE, 299, 13);
/* 62 */     register(context, CREATOR, SoundEvents.MUSIC_DISC_CREATOR, 176, 12);
/* 63 */     register(context, CREATOR_MUSIC_BOX, SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX, 73, 11);
/* 64 */     register(context, TEARS, SoundEvents.MUSIC_DISC_TEARS, 175, 10);
/* 65 */     register(context, LAVA_CHICKEN, SoundEvents.MUSIC_DISC_LAVA_CHICKEN, 134, 9);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\JukeboxSongs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */