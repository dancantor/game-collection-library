using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion.Internal;
using System.Text.Json.Serialization;

namespace GameLibraryAPI.Dto
{
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public enum PlatformType
    {
        PC,
        PS,
        XBOX
    }
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public enum FormatType
    {
        DIGITAL,
        DISK
    }
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public enum CurrencyType
    {
        EUR,
        DOL,
        RON
    }
    public class GameDto
    {
        public int? Id { get; set; }
        public string? Name { get; set; }
        public string? Producer { get; set; }
        public string? PurchaseDate { get; set; }
        public string? LastTimePlayed { get; set; }
        public double? Price { get; set; }
        public CurrencyType? Currency { get; set; }
        public PlatformType? Platform { get; set; }
        public FormatType? FormatOfGame { get; set; }
        public string? PicturePath { get; set; }
    }
}
