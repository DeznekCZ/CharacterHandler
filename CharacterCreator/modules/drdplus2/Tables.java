package drdplus2;

import static core.Attr.v;

import core.Statistic;

public class Tables {
	
	private static int[][] ZZvalue = new int[][] {
		{-4,-3,-3,-2,-2,-1, 0, 0, 1, 2, 2, 3, 4, 5, 6, 6, 7, 8, 9,10,11,12,13,14,15,15},
		{-3,-3,-2,-2,-1,-1, 0, 1, 1, 2, 3, 3, 4, 5, 6, 7, 7, 8, 9,10,11,12,13,14,15,16},
		{-3,-2,-1,-1,-1, 0, 0, 1, 2, 2, 3, 4, 4, 5, 6, 7, 8, 8, 9,10,11,12,13,14,15,16},
		{-2,-2,-1,-1, 0, 0, 1, 1, 2, 3, 3, 4, 5, 5, 6, 7, 8, 9, 9,10,11,12,13,14,15,16},
		{-2,-1,-1, 0, 0, 1, 1, 2, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9,10,10,11,12,13,14,15,16},
		{-1,-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 7, 7, 8, 9,10,11,11,12,13,14,15,16},
		{ 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 8, 8, 9,10,11,12,12,13,14,15,16},
		{ 0, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7, 8, 9, 9,10,11,12,13,13,14,15,16},
		{ 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 9,10,10,11,12,13,14,14,15,16},
		{ 2, 2, 2, 3, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 9, 9,10,11,11,12,13,14,15,15,16},
		{ 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9,10,10,11,12,12,13,14,15,16,16},
		{ 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9,10,11,11,12,13,13,14,15,16,17},
		{ 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 9,10,10,11,12,12,13,14,14,15,16,17},
		{ 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 9, 9,10,10,11,11,12,13,13,14,15,15,16,17},
		{ 6, 6, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9,10,10,11,11,12,12,13,14,14,15,16,16,17},
		{ 6, 7, 7, 7, 7, 7, 8, 8, 8, 9, 9, 9,10,10,11,11,12,12,13,13,14,15,15,16,17,17},
		{ 7, 7, 8, 8, 8, 8, 8, 9, 9, 9,10,10,10,11,11,12,12,13,13,14,14,15,16,16,17,18},
		{ 8, 8, 8, 9, 9, 9, 9, 9,10,10,10,11,11,11,12,12,13,13,14,14,15,15,16,17,17,18},
		{ 9, 9, 9, 9,10,10,10,10,10,11,11,11,12,12,12,13,13,14,14,15,15,16,16,17,18,18},
		{10,10,10,10,10,11,11,11,11,11,12,12,12,13,13,13,14,14,15,15,16,16,17,17,18,19},
		{11,11,11,11,11,11,12,12,12,12,12,13,13,13,14,14,14,15,15,16,16,17,17,18,18,19},
		{12,12,12,12,12,12,12,13,13,13,13,13,14,14,14,15,15,15,16,16,17,17,18,18,19,19},
		{13,13,13,13,13,13,13,13,14,14,14,14,14,15,15,15,16,16,16,17,17,18,18,19,19,20},
		{14,14,14,14,14,14,14,14,14,15,15,15,15,15,16,16,16,17,17,17,18,18,19,19,20,20},
		{15,15,15,15,15,15,15,15,15,15,16,16,16,16,16,17,17,17,18,18,18,19,19,20,20,21},
		{15,16,16,16,16,16,16,16,16,16,16,17,17,17,17,17,18,18,18,19,19,19,20,20,21,21}
	};
	
	public static int ZZ(int zz, int sila)
	{
		return ZZvalue[(zz + 5)][(sila + 5)];
	}
	
	static Integer[] LIFEroot = v(
			10,11,12,14,16,18,20,22,25,28,
			32,36,40,45,50,56,63,70,80,90
			);
	
	public static int LIFE(int kondice) {
		return LIFEroot[((kondice - 10) % 20)] * ((kondice - 10) / 20);
	}
}