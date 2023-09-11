using System;
using System.Collections.Generic;

namespace GameLibraryAPI.Model
{
    public partial class GameLibrary
    {
        public int Id { get; set; }
        public string? Name { get; set; }
        public string? Producer { get; set; }
        public string? PurchaseDate { get; set; }
        public string? LastTimePlayed { get; set; }
        public double? Price { get; set; }
        public string? Currency { get; set; }
        public string? Platform { get; set; }
        public string? FormatOfGame { get; set; }
        public string? PicturePath { get; set; }
    }
}
