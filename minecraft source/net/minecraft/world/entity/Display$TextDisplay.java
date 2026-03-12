/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
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
/*     */ public class TextDisplay
/*     */   extends Display
/*     */ {
/*     */   public static final String TAG_TEXT = "text";
/*     */   private static final String TAG_LINE_WIDTH = "line_width";
/*     */   private static final String TAG_TEXT_OPACITY = "text_opacity";
/*     */   private static final String TAG_BACKGROUND_COLOR = "background";
/*     */   private static final String TAG_SHADOW = "shadow";
/*     */   private static final String TAG_SEE_THROUGH = "see_through";
/*     */   private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background";
/*     */   private static final String TAG_ALIGNMENT = "alignment";
/*     */   public static final byte FLAG_SHADOW = 1;
/*     */   public static final byte FLAG_SEE_THROUGH = 2;
/*     */   public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4;
/*     */   public static final byte FLAG_ALIGN_LEFT = 8;
/*     */   public static final byte FLAG_ALIGN_RIGHT = 16;
/*     */   private static final byte INITIAL_TEXT_OPACITY = -1;
/*     */   public static final int INITIAL_BACKGROUND = 1073741824;
/*     */   private static final int INITIAL_LINE_WIDTH = 200;
/*     */   
/*     */   public enum Align
/*     */     implements StringRepresentable
/*     */   {
/* 660 */     CENTER("center"),
/* 661 */     LEFT("left"),
/* 662 */     RIGHT("right"); public static final Codec<Align> CODEC; private final String name;
/*     */     static  {
/* 664 */       CODEC = StringRepresentable.fromEnum(Align::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 669 */     Align(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 674 */     public String getSerializedName() { return this.name; }
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
/* 697 */   private static final EntityDataAccessor<Component> DATA_TEXT_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.COMPONENT);
/* 698 */   private static final EntityDataAccessor<Integer> DATA_LINE_WIDTH_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/* 699 */   private static final EntityDataAccessor<Integer> DATA_BACKGROUND_COLOR_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/* 700 */   private static final EntityDataAccessor<Byte> DATA_TEXT_OPACITY_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/* 701 */   private static final EntityDataAccessor<Byte> DATA_STYLE_FLAGS_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */   
/* 703 */   private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(new int[] { DATA_TEXT_ID
/* 704 */         .id(), DATA_LINE_WIDTH_ID
/* 705 */         .id(), DATA_BACKGROUND_COLOR_ID
/* 706 */         .id(), DATA_TEXT_OPACITY_ID
/* 707 */         .id(), DATA_STYLE_FLAGS_ID
/* 708 */         .id() });
/*     */ 
/*     */   
/*     */   private CachedInfo clientDisplayCache;
/*     */   
/*     */   private TextRenderState textRenderState;
/*     */ 
/*     */   
/* 716 */   public TextDisplay(EntityType<?> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 721 */     super.defineSynchedData(entityData);
/* 722 */     entityData.define(DATA_TEXT_ID, Component.empty());
/* 723 */     entityData.define(DATA_LINE_WIDTH_ID, Integer.valueOf(200));
/* 724 */     entityData.define(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(1073741824));
/* 725 */     entityData.define(DATA_TEXT_OPACITY_ID, Byte.valueOf((byte)-1));
/* 726 */     entityData.define(DATA_STYLE_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 731 */     super.onSyncedDataUpdated(accessor);
/*     */     
/* 733 */     if (TEXT_RENDER_STATE_IDS.contains(accessor.id())) {
/* 734 */       this.updateRenderState = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 739 */   private Component getText() { return (Component)this.entityData.get(DATA_TEXT_ID); }
/*     */ 
/*     */ 
/*     */   
/* 743 */   private void setText(Component text) { this.entityData.set(DATA_TEXT_ID, text); }
/*     */ 
/*     */ 
/*     */   
/* 747 */   private int getLineWidth() { return ((Integer)this.entityData.get(DATA_LINE_WIDTH_ID)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 751 */   private void setLineWidth(int width) { this.entityData.set(DATA_LINE_WIDTH_ID, Integer.valueOf(width)); }
/*     */ 
/*     */ 
/*     */   
/* 755 */   private byte getTextOpacity() { return ((Byte)this.entityData.get(DATA_TEXT_OPACITY_ID)).byteValue(); }
/*     */ 
/*     */ 
/*     */   
/* 759 */   private void setTextOpacity(byte opacity) { this.entityData.set(DATA_TEXT_OPACITY_ID, Byte.valueOf(opacity)); }
/*     */ 
/*     */ 
/*     */   
/* 763 */   private int getBackgroundColor() { return ((Integer)this.entityData.get(DATA_BACKGROUND_COLOR_ID)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 767 */   private void setBackgroundColor(int color) { this.entityData.set(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(color)); }
/*     */ 
/*     */ 
/*     */   
/* 771 */   private byte getFlags() { return ((Byte)this.entityData.get(DATA_STYLE_FLAGS_ID)).byteValue(); }
/*     */ 
/*     */ 
/*     */   
/* 775 */   private void setFlags(byte flags) { this.entityData.set(DATA_STYLE_FLAGS_ID, Byte.valueOf(flags)); }
/*     */ 
/*     */   
/*     */   private static byte loadFlag(byte flags, ValueInput input, String id, byte mask) {
/* 779 */     if (input.getBooleanOr(id, false)) {
/* 780 */       return (byte)(flags | mask);
/*     */     }
/* 782 */     return flags;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 787 */     super.readAdditionalSaveData(input);
/*     */     
/* 789 */     setLineWidth(input.getIntOr("line_width", 200));
/* 790 */     setTextOpacity(input.getByteOr("text_opacity", (byte)-1));
/* 791 */     setBackgroundColor(input.getIntOr("background", 1073741824));
/*     */ 
/*     */     
/* 794 */     byte flags = loadFlag((byte)0, input, "shadow", (byte)1);
/* 795 */     flags = loadFlag(flags, input, "see_through", (byte)2);
/* 796 */     flags = loadFlag(flags, input, "default_background", (byte)4);
/*     */     
/* 798 */     Optional<Align> alignment = input.read("alignment", Align.CODEC);
/* 799 */     if (alignment.isPresent()) {
/* 800 */       switch (((Align)alignment.get()).ordinal()) { default: throw new MatchException(null, null);
/*     */         case 0: 
/*     */         case 1: 
/* 803 */         case 2: break; }  flags = (byte)(flags | 0x10);
/*     */     } 
/*     */ 
/*     */     
/* 807 */     setFlags(flags);
/*     */     
/* 809 */     Optional<Component> text = input.read("text", ComponentSerialization.CODEC);
/* 810 */     if (text.isPresent()) {
/*     */       try {
/* 812 */         Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 813 */           CommandSourceStack context = createCommandSourceStackForNameResolution(serverLevel).withPermission(LevelBasedPermissionSet.GAMEMASTER);
/* 814 */           MutableComponent mutableComponent = ComponentUtils.updateForEntity(context, (Component)text.get(), this, 0);
/* 815 */           setText(mutableComponent); }
/*     */         else
/* 817 */         { setText(Component.empty()); }
/*     */       
/* 819 */       } catch (Exception e) {
/* 820 */         Display.LOGGER.warn("Failed to parse display entity text {}", text, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 826 */   private static void storeFlag(byte flags, ValueOutput output, String id, byte mask) { output.putBoolean(id, ((flags & mask) != 0)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 831 */     super.addAdditionalSaveData(output);
/* 832 */     output.store("text", ComponentSerialization.CODEC, getText());
/* 833 */     output.putInt("line_width", getLineWidth());
/* 834 */     output.putInt("background", getBackgroundColor());
/* 835 */     output.putByte("text_opacity", getTextOpacity());
/*     */     
/* 837 */     byte flags = getFlags();
/* 838 */     storeFlag(flags, output, "shadow", (byte)1);
/* 839 */     storeFlag(flags, output, "see_through", (byte)2);
/* 840 */     storeFlag(flags, output, "default_background", (byte)4);
/* 841 */     output.store("alignment", Align.CODEC, getAlign(flags));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 846 */     if (shouldInterpolate && this.textRenderState != null) {
/* 847 */       this.textRenderState = createInterpolatedTextRenderState(this.textRenderState, progress);
/*     */     } else {
/* 849 */       this.textRenderState = createFreshTextRenderState();
/*     */     } 
/* 851 */     this.clientDisplayCache = null;
/*     */   }
/*     */ 
/*     */   
/* 855 */   public TextRenderState textRenderState() { return this.textRenderState; }
/*     */ 
/*     */   
/*     */   private TextRenderState createFreshTextRenderState() {
/* 859 */     return new TextRenderState(
/* 860 */         getText(), 
/* 861 */         getLineWidth(), 
/* 862 */         Display.IntInterpolator.constant(getTextOpacity()), 
/* 863 */         Display.IntInterpolator.constant(getBackgroundColor()), 
/* 864 */         getFlags());
/*     */   }
/*     */ 
/*     */   
/*     */   private TextRenderState createInterpolatedTextRenderState(TextRenderState previous, float progress) {
/* 869 */     int currentBackground = previous.backgroundColor.get(progress);
/* 870 */     int currentOpacity = previous.textOpacity.get(progress);
/*     */     
/* 872 */     return new TextRenderState(
/* 873 */         getText(), 
/* 874 */         getLineWidth(), new Display.LinearIntInterpolator(currentOpacity, 
/* 875 */           getTextOpacity()), new Display.ColorInterpolator(currentBackground, 
/* 876 */           getBackgroundColor()), 
/* 877 */         getFlags());
/*     */   }
/*     */   public static final class CachedLine extends Record { private final FormattedCharSequence contents; private final int width;
/*     */     
/* 881 */     public CachedLine(FormattedCharSequence contents, int width) { this.contents = contents; this.width = width; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 881 */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public FormattedCharSequence contents() { return this.contents; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/* 881 */       //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } }
/*     */   public static final class CachedInfo extends Record { private final List<Display.TextDisplay.CachedLine> lines; private final int width;
/* 883 */     public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) { this.lines = lines; this.width = width; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/* 883 */       //   0	8	1	o	Ljava/lang/Object; } public List<Display.TextDisplay.CachedLine> lines() { return this.lines; } public int width() { return this.width; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachedInfo cacheDisplay(LineSplitter splitter) {
/* 891 */     if (this.clientDisplayCache == null) {
/* 892 */       if (this.textRenderState != null) {
/* 893 */         this.clientDisplayCache = splitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
/*     */       } else {
/* 895 */         this.clientDisplayCache = new CachedInfo(List.of(), 0);
/*     */       } 
/*     */     }
/*     */     
/* 899 */     return this.clientDisplayCache;
/*     */   }
/*     */   
/*     */   public static Align getAlign(byte flags) {
/* 903 */     if ((flags & 0x8) != 0) {
/* 904 */       return Align.LEFT;
/*     */     }
/* 906 */     if ((flags & 0x10) != 0) {
/* 907 */       return Align.RIGHT;
/*     */     }
/* 909 */     return Align.CENTER;
/*     */   }
/*     */   public static final class TextRenderState extends Record { private final Component text; private final int lineWidth; private final Display.IntInterpolator textOpacity; private final Display.IntInterpolator backgroundColor; private final byte flags;
/* 912 */     public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) { this.text = text; this.lineWidth = lineWidth; this.textOpacity = textOpacity; this.backgroundColor = backgroundColor; this.flags = flags; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/* 912 */       //   0	8	1	o	Ljava/lang/Object; } public Component text() { return this.text; } public int lineWidth() { return this.lineWidth; } public Display.IntInterpolator textOpacity() { return this.textOpacity; } public Display.IntInterpolator backgroundColor() { return this.backgroundColor; } public byte flags() { return this.flags; } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LineSplitter {
/*     */     Display.TextDisplay.CachedInfo split(Component param2Component, int param2Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Display$TextDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */