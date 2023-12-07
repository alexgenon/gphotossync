package be.genon

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GoogleDeserTest {
    @Test
    fun deser(){
        val mediaItems = Json.decodeFromString<MediaItems>(sampleResponse)
        Assertions.assertEquals(2,mediaItems.mediaItems.size)
        val firstPhoto = mediaItems.mediaItems[0]
        Assertions.assertEquals(
            "IMG_0827.HEIC",
            firstPhoto.filename
        )
        Assertions.assertEquals(
            LocalDateTime.parse("2023-04-08T13:17:05").toInstant(TimeZone.UTC),
            firstPhoto.mediaMetadata.creationTime
        )
        Assertions.assertEquals("abc",mediaItems.nextPageToken)
    }

    @Test
    fun standardize(){
        val mediaItems = Json.decodeFromString<MediaItems>(sampleResponse)
        val firstPhoto = mediaItems.mediaItems[0].toStandardMedia()
        Assertions.assertEquals(
            8,
            firstPhoto.creationTime?.dayOfMonth
        )
        Assertions.assertEquals(
            Month.APRIL,
            firstPhoto.creationTime?.month
        )
        Assertions.assertEquals("abc",mediaItems.nextPageToken)
    }

    @Test
    fun deserFinalTest(){
        val mediaItems = Json.decodeFromString<MediaItems>(sampleResponseFinalPage)
        Assertions.assertEquals(2,mediaItems.mediaItems.size)
        Assertions.assertNull(mediaItems.nextPageToken)
    }

    private val sampleResponse=""" 
        { 
        "mediaItems": [
            {
              "id": "AE5Sh638eIeRUG8RpRajogjQ_kfYZFOIZp6j5eySpg9JDPoN2QxqWMa6huTi0VpqFI5667_Hj6Xs5u9-H9w3ZvWQa2HrYw0_GQ",
              "productUrl": "https://photos.google.com/lr/photo/AE5Sh638eIeRUG8RpRajogjQ_kfYZFOIZp6j5eySpg9JDPoN2QxqWMa6huTi0VpqFI5667_Hj6Xs5u9-H9w3ZvWQa2HrYw0_GQ",
              "baseUrl": "https://lh3.googleusercontent.com/lr/AHRh2pb--KW4-GG0cTFZBxmnuIc-_YiCdUjZHY-q45yEGKjJTczKUSeK1EaOqmkw8yeKKcKqOjNWuSwWdUbOpH6CGEzLIHPxgj_cPG-hlgIWOjmca19riBtvnqmc1wjMSvOqpmw46MHjlqUDd53_5H-qEio70MCaFXWGA9JvG68dWoZt4Tc-V9ukLJW-52Wlby0e0KJGI_Alx7lcX47eewb_-JGmk67Jp4Nap2NZwKNdH7Q_jlaWP9Qp0458DK1-qfCZ59NGaJGoNG0-s11_KoRC8INSuxuJM6i4_qAXvAab1huccsxFfuerY78VtMlLX9zPV2ZsEh84D2mus7u8LwE2vUJbR6ia6X6bnNXzF-w8C1Mf6EEDV6kj9z2DH8sXG3wHuL7dlNolx4SIc3I6qo107APFjnx2Y_TfWTpu0kvWab7sm9PlAOu65FYLH35LleezFb2FnrSWq3mc33Lw90M8_Zww_e_oSPiTUBEnhQ5PNIpv1iIRdSv8diab7Fn1NeZMpSlzz7uQwRX8ipZCH_RURusVArjyruPw9c1AO7sxpCmaDthv0RP8Cy2vgOcJaRuoVI0WAobOPXGf5Com1muWnyh7YgGBOEl1psWA5P-HX9ynt1ptryLqksdQxK5GUpJLCej0cfBoWe7nuP2-zAxjQ9DUxnreIR_LtbiwvwH4ZPK3gTgwcvfYrLm7ZaXmPbWf1QtPJ6gW-1AB2hAn05f8LAi2dSVBwYFPI2uJdqRr6HfVmae7GlhfMl8H5PAPr6UyTOGwfcaHd-MDQpf7or_osP38jR7WclZo9BS5N8rVJYLaNW1KQIsNVYRqXVxN7ro_B1B61by7SoJ-WjajHDm7-Dz5Lj2yErkUqZN4tk64dh9S6ri1GvrMeaaq28n8elqPy3LaeksPe446-aFm3jVzp_0eNs1Z_LOzZ-QQ0xTdOhfwWtgTu_-wX9jzcaSFRoji9zvWruJC1AhxIsSFqtwe4rvWvWFeArMWgrtp48J3goLdNsp3pLU",
              "mimeType": "image/heif",
              "mediaMetadata": {
                "creationTime": "2023-04-08T13:17:05Z",
                "width": "2316",
                "height": "3088",
                "photo": {
                  "cameraMake": "Apple",
                  "cameraModel": "iPhone 14 Pro",
                  "focalLength": 2.69,
                  "apertureFNumber": 1.9,
                  "isoEquivalent": 25,
                  "exposureTime": "0.002016128s"
                }
              },
              "filename": "IMG_0827.HEIC"
            },
            {
              "id": "AE5Sh62aWn9NiUs-XQp7svJL27jSlEzCujK3M7SRTTmUsRc2eAT3Rb806NYnGoJ8dauZcHkPLbU98hE7PzSJ683KGzs2MWxhSQ",
              "productUrl": "https://photos.google.com/lr/photo/AE5Sh62aWn9NiUs-XQp7svJL27jSlEzCujK3M7SRTTmUsRc2eAT3Rb806NYnGoJ8dauZcHkPLbU98hE7PzSJ683KGzs2MWxhSQ",
              "baseUrl": "https://lh3.googleusercontent.com/lr/AHRh2paBuop4vmWR88kSutNTgacvrWRglaY5SVQx2nJWJKuEzWvQi7PDKH8ntnGQawaBqEVfNweIQkVV2iPO3YJ2LgD-VXTpWlctvHczvEteyQwT65ZqoM1_B5AQjw_d0MN1LuxVwTzmrO30x5egKH908P7q1sIqN7-tSJEx_smk_UKd8aLvsu0XbBIzu-cw24EcWcyWNurGMj6JFBcVimp9EUtdE5wYAezNnTFNpg6chd_xB6mmExWCShrHr4GsAGxrSGHU1HEhwkhceRvAnJ9yUyLBB538p63a0Cjy9-sWljEIT9plmP27OLndFOhQCl0t49iXjOq5vs0exMToXOFFmmMp9KpUYAB1ojeZZxrd7pO_l4ZX693p_Cm6IWwqw0XlzojhBNtjmLqeDok9jK5R1y6pCf4BEdxK88-xEVJauaB6f9gSMvzufQDHpAIOtiG1AXlpTkCxk3dmbZn5jxHkf3wWaaSpRuHl_yu3c7MC7lYzxUbuVUs8Vyuf8VbVIEjdtDJaC4_40sh5mo1iso4PgsnTTmxOfSqg6wIJzWLRM7EyEy8YPERVfSQvuzgNSFhuBAdk3t6_F9She19rOHeSdSUxB8AI0RVY1pZHEZungOjDFe2DmIK4qc0vulAZUJpy6_5a_s2GPryRHfK3eYBhcPwz4uTglOhtbpkYZlDqt7XZcmaj8C8ZfhvmJMAz634cGgq9eCxgSt8H7vv-1HOk8RTPKER7CGPSKB8u5aGUMHJDsJjB2XzcAD1enzZoacK1Nf4aAZQsSBL5w8SJ9RB9kzqYkas2ArMMEKEy3utaF2RijQp-vbq4MpGIZ9yq-2qxsrZzGSXP5YEfuLRuzYkkD8CNH7qtEGUvgOw4qbzT-zd2NVrg3pCQPZ0hSVmvuwnwjV6RLvi9h7U-noJZzik2Zf38oOpGfnzBFyZeKsTWaUtgWQN3UC3A-OYYUJgukW9zzFDrFtyNwGvt0pcpQFp31pKu-QXxSvW8txGXLUF82NchAGLhKmw",
              "mimeType": "video/mp4",
              "mediaMetadata": {
                "creationTime": "2023-04-08T13:10:07Z",
                "width": "1920",
                "height": "1080",
                "video": {
                  "cameraMake": "Apple",
                  "cameraModel": "iPhone 14 Pro",
                  "fps": 29.994340690435767,
                  "status": "READY"
                }
              },
              "filename": "IMG_0826.MOV"
            }
    ],
    "nextPageToken": "abc"
}
""".trimIndent()
    private val sampleResponseFinalPage=""" 
        { 
        "mediaItems": [
            {
              "id": "AE5Sh638eIeRUG8RpRajogjQ_kfYZFOIZp6j5eySpg9JDPoN2QxqWMa6huTi0VpqFI5667_Hj6Xs5u9-H9w3ZvWQa2HrYw0_GQ",
              "productUrl": "https://photos.google.com/lr/photo/AE5Sh638eIeRUG8RpRajogjQ_kfYZFOIZp6j5eySpg9JDPoN2QxqWMa6huTi0VpqFI5667_Hj6Xs5u9-H9w3ZvWQa2HrYw0_GQ",
              "baseUrl": "https://lh3.googleusercontent.com/lr/AHRh2pb--KW4-GG0cTFZBxmnuIc-_YiCdUjZHY-q45yEGKjJTczKUSeK1EaOqmkw8yeKKcKqOjNWuSwWdUbOpH6CGEzLIHPxgj_cPG-hlgIWOjmca19riBtvnqmc1wjMSvOqpmw46MHjlqUDd53_5H-qEio70MCaFXWGA9JvG68dWoZt4Tc-V9ukLJW-52Wlby0e0KJGI_Alx7lcX47eewb_-JGmk67Jp4Nap2NZwKNdH7Q_jlaWP9Qp0458DK1-qfCZ59NGaJGoNG0-s11_KoRC8INSuxuJM6i4_qAXvAab1huccsxFfuerY78VtMlLX9zPV2ZsEh84D2mus7u8LwE2vUJbR6ia6X6bnNXzF-w8C1Mf6EEDV6kj9z2DH8sXG3wHuL7dlNolx4SIc3I6qo107APFjnx2Y_TfWTpu0kvWab7sm9PlAOu65FYLH35LleezFb2FnrSWq3mc33Lw90M8_Zww_e_oSPiTUBEnhQ5PNIpv1iIRdSv8diab7Fn1NeZMpSlzz7uQwRX8ipZCH_RURusVArjyruPw9c1AO7sxpCmaDthv0RP8Cy2vgOcJaRuoVI0WAobOPXGf5Com1muWnyh7YgGBOEl1psWA5P-HX9ynt1ptryLqksdQxK5GUpJLCej0cfBoWe7nuP2-zAxjQ9DUxnreIR_LtbiwvwH4ZPK3gTgwcvfYrLm7ZaXmPbWf1QtPJ6gW-1AB2hAn05f8LAi2dSVBwYFPI2uJdqRr6HfVmae7GlhfMl8H5PAPr6UyTOGwfcaHd-MDQpf7or_osP38jR7WclZo9BS5N8rVJYLaNW1KQIsNVYRqXVxN7ro_B1B61by7SoJ-WjajHDm7-Dz5Lj2yErkUqZN4tk64dh9S6ri1GvrMeaaq28n8elqPy3LaeksPe446-aFm3jVzp_0eNs1Z_LOzZ-QQ0xTdOhfwWtgTu_-wX9jzcaSFRoji9zvWruJC1AhxIsSFqtwe4rvWvWFeArMWgrtp48J3goLdNsp3pLU",
              "mimeType": "image/heif",
              "mediaMetadata": {
                "creationTime": "2023-04-08T13:17:05Z",
                "width": "2316",
                "height": "3088",
                "photo": {
                  "cameraMake": "Apple",
                  "cameraModel": "iPhone 14 Pro",
                  "focalLength": 2.69,
                  "apertureFNumber": 1.9,
                  "isoEquivalent": 25,
                  "exposureTime": "0.002016128s"
                }
              },
              "filename": "IMG_0827.HEIC"
            },
            {
              "id": "AE5Sh62aWn9NiUs-XQp7svJL27jSlEzCujK3M7SRTTmUsRc2eAT3Rb806NYnGoJ8dauZcHkPLbU98hE7PzSJ683KGzs2MWxhSQ",
              "productUrl": "https://photos.google.com/lr/photo/AE5Sh62aWn9NiUs-XQp7svJL27jSlEzCujK3M7SRTTmUsRc2eAT3Rb806NYnGoJ8dauZcHkPLbU98hE7PzSJ683KGzs2MWxhSQ",
              "baseUrl": "https://lh3.googleusercontent.com/lr/AHRh2paBuop4vmWR88kSutNTgacvrWRglaY5SVQx2nJWJKuEzWvQi7PDKH8ntnGQawaBqEVfNweIQkVV2iPO3YJ2LgD-VXTpWlctvHczvEteyQwT65ZqoM1_B5AQjw_d0MN1LuxVwTzmrO30x5egKH908P7q1sIqN7-tSJEx_smk_UKd8aLvsu0XbBIzu-cw24EcWcyWNurGMj6JFBcVimp9EUtdE5wYAezNnTFNpg6chd_xB6mmExWCShrHr4GsAGxrSGHU1HEhwkhceRvAnJ9yUyLBB538p63a0Cjy9-sWljEIT9plmP27OLndFOhQCl0t49iXjOq5vs0exMToXOFFmmMp9KpUYAB1ojeZZxrd7pO_l4ZX693p_Cm6IWwqw0XlzojhBNtjmLqeDok9jK5R1y6pCf4BEdxK88-xEVJauaB6f9gSMvzufQDHpAIOtiG1AXlpTkCxk3dmbZn5jxHkf3wWaaSpRuHl_yu3c7MC7lYzxUbuVUs8Vyuf8VbVIEjdtDJaC4_40sh5mo1iso4PgsnTTmxOfSqg6wIJzWLRM7EyEy8YPERVfSQvuzgNSFhuBAdk3t6_F9She19rOHeSdSUxB8AI0RVY1pZHEZungOjDFe2DmIK4qc0vulAZUJpy6_5a_s2GPryRHfK3eYBhcPwz4uTglOhtbpkYZlDqt7XZcmaj8C8ZfhvmJMAz634cGgq9eCxgSt8H7vv-1HOk8RTPKER7CGPSKB8u5aGUMHJDsJjB2XzcAD1enzZoacK1Nf4aAZQsSBL5w8SJ9RB9kzqYkas2ArMMEKEy3utaF2RijQp-vbq4MpGIZ9yq-2qxsrZzGSXP5YEfuLRuzYkkD8CNH7qtEGUvgOw4qbzT-zd2NVrg3pCQPZ0hSVmvuwnwjV6RLvi9h7U-noJZzik2Zf38oOpGfnzBFyZeKsTWaUtgWQN3UC3A-OYYUJgukW9zzFDrFtyNwGvt0pcpQFp31pKu-QXxSvW8txGXLUF82NchAGLhKmw",
              "mimeType": "video/mp4",
              "mediaMetadata": {
                "creationTime": "2023-04-08T13:10:07Z",
                "width": "1920",
                "height": "1080",
                "video": {
                  "cameraMake": "Apple",
                  "cameraModel": "iPhone 14 Pro",
                  "fps": 29.994340690435767,
                  "status": "READY"
                }
              },
              "filename": "IMG_0826.MOV"
            }
    ]
}
""".trimIndent()
}
