/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SlotRanges
/*     */ {
/*  23 */   private static final List<SlotRange> SLOTS = (List)Util.make(new ArrayList(), values -> {
/*  24 */         addSingleSlot(values, "contents", 0);
/*  25 */         addSlotRange(values, "container.", 0, 54);
/*  26 */         addSlotRange(values, "hotbar.", 0, 9);
/*  27 */         addSlotRange(values, "inventory.", 9, 27);
/*  28 */         addSlotRange(values, "enderchest.", 200, 27);
/*  29 */         addSlotRange(values, "villager.", 300, 8);
/*  30 */         addSlotRange(values, "horse.", 500, 15);
/*     */ 
/*     */         
/*  33 */         int mainhand = EquipmentSlot.MAINHAND.getIndex(98);
/*  34 */         int offhand = EquipmentSlot.OFFHAND.getIndex(98);
/*  35 */         addSingleSlot(values, "weapon", mainhand);
/*  36 */         addSingleSlot(values, "weapon.mainhand", mainhand);
/*  37 */         addSingleSlot(values, "weapon.offhand", offhand);
/*  38 */         addSlots(values, "weapon.*", new int[] { mainhand, offhand });
/*     */ 
/*     */ 
/*     */         
/*  42 */         int head = EquipmentSlot.HEAD.getIndex(100);
/*  43 */         int chest = EquipmentSlot.CHEST.getIndex(100);
/*  44 */         int legs = EquipmentSlot.LEGS.getIndex(100);
/*  45 */         int feet = EquipmentSlot.FEET.getIndex(100);
/*  46 */         int body = EquipmentSlot.BODY.getIndex(105);
/*     */         
/*  48 */         addSingleSlot(values, "armor.head", head);
/*  49 */         addSingleSlot(values, "armor.chest", chest);
/*  50 */         addSingleSlot(values, "armor.legs", legs);
/*  51 */         addSingleSlot(values, "armor.feet", feet);
/*  52 */         addSingleSlot(values, "armor.body", body);
/*  53 */         addSlots(values, "armor.*", new int[] { head, chest, legs, feet, body });
/*     */         
/*  55 */         addSingleSlot(values, "saddle", EquipmentSlot.SADDLE.getIndex(106));
/*     */ 
/*     */         
/*  58 */         addSingleSlot(values, "horse.chest", 499);
/*     */         
/*  60 */         addSingleSlot(values, "player.cursor", 499);
/*  61 */         addSlotRange(values, "player.crafting.", 500, 4);
/*     */       });
/*     */   
/*  64 */   public static final Codec<SlotRange> CODEC = StringRepresentable.fromValues(() -> (SlotRange[])SLOTS.toArray(()));
/*     */   
/*  66 */   private static final Function<String, SlotRange> NAME_LOOKUP = StringRepresentable.createNameLookup((SlotRange[])SLOTS.toArray(x$0 -> new SlotRange[x$0]));
/*     */ 
/*     */   
/*  69 */   private static SlotRange create(String name, int id) { return SlotRange.of(name, IntLists.singleton(id)); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static SlotRange create(String name, IntList ids) { return SlotRange.of(name, IntLists.unmodifiable(ids)); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static SlotRange create(String name, int... ids) { return SlotRange.of(name, IntList.of(ids)); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static void addSingleSlot(List<SlotRange> output, String name, int id) { output.add(create(name, id)); }
/*     */ 
/*     */   
/*     */   private static void addSlotRange(List<SlotRange> output, String prefix, int offset, int size) {
/*  85 */     IntArrayList intArrayList = new IntArrayList(size);
/*  86 */     for (int i = 0; i < size; i++) {
/*  87 */       int slotId = offset + i;
/*  88 */       output.add(create(prefix + prefix, slotId));
/*  89 */       intArrayList.add(slotId);
/*     */     } 
/*  91 */     output.add(create(prefix + "*", intArrayList));
/*     */   }
/*     */ 
/*     */   
/*  95 */   private static void addSlots(List<SlotRange> output, String name, int... values) { output.add(create(name, values)); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static SlotRange nameToIds(String name) { return (SlotRange)NAME_LOOKUP.apply(name); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static Stream<String> allNames() { return SLOTS.stream().map(StringRepresentable::getSerializedName); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static Stream<String> singleSlotNames() { return SLOTS.stream().filter(e -> (e.size() == 1)).map(StringRepresentable::getSerializedName); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\SlotRanges.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */