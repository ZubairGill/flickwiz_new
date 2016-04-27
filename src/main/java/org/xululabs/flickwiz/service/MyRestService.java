package org.xululabs.flickwiz.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.imgproc.Imgproc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xululabs.flickwiz.service.coreclassess.Converter;
import org.xululabs.flickwiz.service.coreclassess.FeaturesORB;
import org.xululabs.flickwiz.service.coreclassess.MatDecoderAndEncoder;
import org.xululabs.flickwiz.service.coreclassess.SimilarityIndex;
import org.xululabs.flickwiz.service.coreclassess.URLFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;

									/*
									 * This is new Service to test the large data with new implementation.. 
									 */	
			

@RestController
@RequestMapping(value = "/flickwiz")
@ComponentScan("serverPack")
public class MyRestService {
	
	
	private SimilarityIndex best[];
	private static final LinkedList<URL> posterUrls = new LinkedList<URL>();
	private static final LinkedList<String> posterNames = new LinkedList<String>();
	private static final LinkedList<Mat> posters_TrainDescriptors = new LinkedList<Mat>();
	private LinkedList<URL> bestURLS = new LinkedList<URL>();
	private LinkedList<String> bestNames = new LinkedList<String>();
	private LinkedList<LinkedList<String>> IMDBDetials = new LinkedList<LinkedList<String>>();
	private final ArrayList<LinkedList<String>> movieList = new ArrayList<LinkedList<String>>();
	private ArrayList<String> tempList = new ArrayList();
	private int count = 0;

	private DescriptorMatcher descriptorMatcher;
	private FeaturesORB featuresORB;
	private Mat queryDescriptor;
	private Mat trainDescriptor;
	private MatOfDMatch matches;
	private static boolean startFirstTime = true;
	
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public @ResponseBody ResponseModel getFeatureResult(
			@RequestBody FlickwizImage uploadedImage) throws IOException {
		System.out.println("Request Received on path /uploadImage" +"[ "+ Calendar.getInstance().getTime()+" ]" );
		System.out.println(uploadedImage);
		best=new SimilarityIndex[15];
		best[0]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[1]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[2]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[3]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[4]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[5]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[6]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[7]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[8]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[9]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[10]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[11]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[12]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[13]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		best[14]=new SimilarityIndex(101.0,URLFactory.create("http://ia.media-imdb.com/images/M/MV5BMjQwOTc0Mzg3Nl5BMl5BanBnXkFtZTgwOTg3NjI2NzE@._V1__SX640_SY720_.jpg"),"abc");
		
		Loader.init();
		
		
		SimilarityIndex posterSimilarity=new SimilarityIndex();
		FeaturesORB orb = new FeaturesORB();
		Mat queryDescriptor = new Mat();
		MatOfDMatch matches = new MatOfDMatch();
		DescriptorMatcher	descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		
		try {
			long start=System.currentTimeMillis();
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(uploadedImage.getBase64Code());
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			System.out.println("Query image dimensions : "+img.getWidth() + " * " + img.getHeight());
			queryDescriptor = orb.getORBFeaturesDescriptorMat(Converter.img2Mat(img));
			
			
			CSVReader csvReader = new CSVReader(new FileReader("movieFile/imdbfeatures.csv"), ',');
			//CSVReader csvReader = new CSVReader(new FileReader("movieFile/sample.csv"), ',');
			String[] row;
			
			int rw = 0;
			int cl = 0;
			String name = null;
			URL url = null;
			String mat=null;
			String channel=null;
			int matrow=-1;
			Mat finalMat =null;
			double similarity = 101.0;
			double y = 0.11;
			List<SimilarityIndex> bestMatches=new ArrayList<SimilarityIndex>();
			SimilarityIndex bestMatch=new SimilarityIndex();
			
			while((row=csvReader.readNext())!=null)
			{
				////////Check the details ////
				if(row[5].equals("1"))
				{	
					name=row[0];
					url=new URL(row[1]);
					mat=row[2];	
					rw=Integer.parseInt(row[3]);
					cl=Integer.parseInt(row[4]);
					channel=row[5];
					finalMat=MatDecoderAndEncoder.decode(mat,rw,cl,Integer.parseInt(channel));
					//System.out.println( "[ "+name+" ]"+"[ "+url+" ]");
					if(finalMat != null){
						long startt=System.currentTimeMillis();
						
						double similarityRatio=getSimilarity(queryDescriptor,finalMat);	
						//System.err.println("iteration no : "+ (count++)+"  Similarity ratio : "+similarityRatio);
					
				
					
						double temp=getMaxValue(best);
						if(similarityRatio<temp)
						{
							//bestMatches.add(new SimilarityIndex(similarityRatio, url, name));
						
							int pos=getMaxPosition(temp);
							best[pos]=new SimilarityIndex(similarityRatio, url, name);
							
							
						} 
						
						long endd=System.currentTimeMillis();
					//	System.out.println("Time taken by each is :"+ (endd-startt)+" milisecond.");
					}
				}
					/*
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
			}
			
			
			bestMatches=Arrays.asList(best);
		

			
			Comparator<SimilarityIndex> indexComparator = new Comparator<SimilarityIndex>() {
				public int compare(SimilarityIndex index1,
						SimilarityIndex index2) {
					return index1.getIndex().compareTo(index2.getIndex());
				}
			};
			
			Collections.sort(bestMatches, indexComparator);
			
			bestURLS.clear();
			bestNames.clear();
			IMDBDetials.clear();
			tempList.clear();

			try {
				count=0;
				for (int i = 0; i < bestMatches.size(); i++) {
						
					if (!tempList.contains(bestMatches.get(i).getName()
							.toString())) {

						bestNames.add(bestMatches.get(i).getName());
						bestURLS.add(bestMatches.get(i).getUrl());
						IMDBDetials.add(getImdbData(bestMatches.get(i).getName()));
						tempList.add(bestMatches.get(i).getName());
						++count;
						
						
					}
					if (count == 5) {
						System.out.println("Total movies in result : "+count);
						
						count = 0;
						break;
					}
				}
				
				

			} catch (Exception e) {
				System.out.println(e.getMessage() + " Passing data to List");
			}
			
			for(int j=0;j<bestMatches.size();j++)
			{
				System.err.println(bestMatches.get(j).toString());
				
			}
				
				//System.out.println(bestMatch.toString());
				long end=System.currentTimeMillis();
				System.out.println("Time taken by complete file :"+ (end-start)+" milisecond.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		return new ResponseModel(bestNames, bestURLS, IMDBDetials);
	}


	/*
	 * 	This function is used to get IMDB movie details.
	 */
	private LinkedList<String> getImdbData(String movie) {
		final LinkedList<String> dataIMDB = new LinkedList<>();
		dataIMDB.clear();
		try {
			InputStream input = new URL("http://www.omdbapi.com/?t="
					+ URLEncoder.encode(movie, "UTF-8")).openStream();
			Map<String, String> map = new Gson().fromJson(
					new InputStreamReader(input, "UTF-8"),
					new TypeToken<Map<String, String>>() {
					}.getType());

			dataIMDB.add(map.get("Title"));
			dataIMDB.add(map.get("Year"));
			dataIMDB.add(map.get("Released"));
			dataIMDB.add(map.get("Runtime"));
			dataIMDB.add(map.get("Genre"));
			dataIMDB.add(map.get("Director"));
			dataIMDB.add(map.get("Writer"));
			dataIMDB.add(map.get("Actors"));
			dataIMDB.add(map.get("Plot"));
			dataIMDB.add(map.get("imdbRating"));
			dataIMDB.add(map.get("imdbID"));

			dataIMDB.addFirst("http://www.imdb.com/title/"
					+ map.get("imdbID").toString() + "/");

		} catch (Exception e) {
			System.err.println("The Error is occured while getting data from IMDB..");
			System.out.println(e.getMessage().toString());
		}
		return dataIMDB;
	}

	/*
	 * 	This function read the movies poster from the CSV file and extract features and store them in the LinkedList.
	 */
	private void allFeaturesExtraction() throws IOException {
		int counter = 0;
		// Loader.init();
		featuresORB = new FeaturesORB();
		String[] nextLine;
		// String checkString = new String();
		
		CSVReader reader = new CSVReader(new FileReader("movieFile/movies.csv"), ',','\"', 1);
		//List content=reader.readAll();
	
		//String[] row=null;
		
		//for (Object object : content) {
		 //   row = (String[]) object; 
		  //  System.out.println(Arrays.asList(row));
		//}
	
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			
			String imageUrl = (String.valueOf(nextLine[1].charAt(0)).equals(
					"\"") ? nextLine[1].substring(1, nextLine[1].length() - 1)
					: nextLine[1]);

			String imageName = (String.valueOf(nextLine[0].charAt(0)).equals(
					"\"") ? nextLine[0].substring(1, nextLine[0].length() - 1)
					: nextLine[0]);

			
			
			Mat mat=new Mat();
			mat=Converter.img2Mat(ImageIO.read(new URL(imageUrl)));
			Imgproc.resize(mat, mat, new Size(450,600));
			posters_TrainDescriptors.add(counter, featuresORB
					.getORBFeaturesDescriptorMat(mat));

			/*
			 * You can uncomment these lines if you to see that csv is parsed correctly
			 */
			System.out.println("Name ==> "+imageName );
			System.out.println("Url ==> "+imageUrl );
			posterNames.add(counter, imageName);
			posterUrls.add(counter, new URL(imageUrl));
			++counter;

		} 
		
		reader.close();
	}

	/*
	 * 	This service provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	@RequestMapping(value = "/genredetail", method = RequestMethod.GET)
	public @ResponseBody GenreModel movieListByGenre(String genre) {

		System.out.println("Request Received on path /genredetail");
		System.out.println(genre);

		movieList.clear();
		String genreCode = getGenreCode(genre);
		
			try {

				InputStream input = new URL(
						"https://api.themoviedb.org/3/discover/movie?with_genres="
								+ genreCode
								+ "&api_key=746bcc0040f68b8af9d569f27443901f")
						.openStream();

				Map<String, Object> response = toMapObject(IOUtils.toString(
						input, "UTF-8"));
				if (response == null) {
					System.out.println("Response is null!!");
				} else {
					List<Map<String, Object>> filmsArray = (ArrayList<Map<String, Object>>) response.get("results");

					for (int i = 0; i < 10; i++) {

						final LinkedList<String> movieInfoList = new LinkedList<String>();

						if (filmsArray.get(i).get("poster_path").toString() == null) {
							movieInfoList.add("No Poster Available");
						} else {
							movieInfoList.add("http://image.tmdb.org/t/p/w300"
									+ filmsArray.get(i).get("poster_path")
											.toString());
						}
						if (filmsArray.get(i).get("title").toString() == null) {
							movieInfoList.add("No Title Available");
						} else {
							movieInfoList.add(filmsArray.get(i).get("title")
									.toString());
						}
						if (filmsArray.get(i).get("release_date").toString() == null) {
							movieInfoList.add("No Release Date Available");
						} else {
							movieInfoList.add(filmsArray.get(i)
									.get("release_date").toString());
						}
						if (filmsArray.get(i).get("overview").toString() == null) {
							movieInfoList.add("No overview Available");
						} else {
							movieInfoList.add(filmsArray.get(i).get("overview")
									.toString());
						}
						movieList.add(movieInfoList);
					}
				}

			} catch (Exception e) {

				System.out.println(e.getMessage().toString() + "Error in genre details service");
			}
		return new GenreModel(movieList);
	}

	/*
	 * 	This service  provides the detail of writers,directors and actors when name is passed as input parameter.
	 */
	@RequestMapping(value = "/persondetail", method = RequestMethod.GET)
	public @ResponseBody PersonDetailModel personDetail(String personName) {
		System.out.println("Request Received on path /persondetail");
		System.out.println(personName);

		final LinkedList<String> actorsInfoList = new LinkedList<String>();
		actorsInfoList.clear();

		List<String> tmdbId = new ArrayList<String>();
		List<String> tmdbDOB = new ArrayList<String>();

		String personCode = personResource(toTrim(personName));
		try {
			InputStream input = new URL("http://imdb.wemakesites.net/api/"
					+ URLEncoder.encode(personCode, "UTF-8")).openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));
			Map<String, Object> data = (Map<String, Object>) response
					.get("data");
			List<Object> mediaLinks = (ArrayList<Object>) data
					.get("mediaLinks");
			List<Map<String, Object>> filmography = (ArrayList<Map<String, Object>>) data
					.get("filmography");
			if (response == null) {
				System.out.println("The Responce is null !!! ");
			} else {

				if (data.get("id") == null) {
					actorsInfoList.add("no id available");
				} else {
					actorsInfoList.add((String) data.get("id"));
				}
				if (data.get("title") == null) {
					actorsInfoList.add("no title available");
				} else {
					actorsInfoList.add((String) data.get("title"));
				}
				if (data.get("image") == null) {
					actorsInfoList.add("no image available");
				} else {
					actorsInfoList.add((String) data.get("image"));
				}
				if (data.get("description") == null) {
					actorsInfoList.add("no description available");
				} else {
					actorsInfoList.add((String) data.get("description"));
				}
				tmdbId = getTMDBId(actorsInfoList.getFirst().toString());

				for (int i = 0; i < tmdbId.size(); i++) {
					actorsInfoList.add(tmdbId.get(i).toString());
				}

				tmdbDOB = getDOBInfo(tmdbId.get(0));

				for (int i = 0; i < tmdbDOB.size(); i++) {
					actorsInfoList.add(tmdbDOB.get(i).toString());
				}

			}
		} catch (Exception e) {

			System.out.println(e.getMessage().toString() + " error ");
		}

		return new PersonDetailModel(actorsInfoList);
	}
	
	/*
	 * 	This function provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	private String personResource(String name) {
		String actorCode = "";
		try {
			InputStream input = new URL(
					"http://imdb.wemakesites.net/api/search?q="
							+ URLEncoder.encode(name, "UTF-8")).openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));
			if (response == null) {
				System.out.println("Response is null!!");
			} else {
				Map<String, Object> data = (Map<String, Object>) response
						.get("data");
				Map<String, Object> results = (Map<String, Object>) data
						.get("results");
				List<Map<String, Object>> names = (ArrayList<Map<String, Object>>) results
						.get("names");
				actorCode = names.get(0).get("id").toString();
			}

		} catch (Exception e) {

			System.out.println(e.getMessage().toString() + " error ");
		}

		return actorCode.toString();
	}

	/*
	 * 	This function provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	private List<String> getTMDBId(String imdbId) {
		List<String> tmdbData = new ArrayList<String>();
		try {
			URL url = new URL(
					"https://api.themoviedb.org/3/find/"
							+ imdbId
							+ "?external_source=imdb_id&api_key=3eaf57ed7c6daae4f7ef9c460134ac0f");
			if (url == null) {
				System.out.println("url returned null");
				System.out.println("Url is  null!!");
				throw new NullPointerException();
			}
			System.out.println("Url is not null!!");
			InputStream input = url.openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));

			if (response == null) {
				System.out.println("Response is null!!");
				tmdbData.add("No ID Found");
				tmdbData.add("No Image");
				tmdbData.add("No Image");
				tmdbData.add("No Image");
			} else {

				List<Map<String, Object>> personResult = (ArrayList<Map<String, Object>>) response.get("person_results");
				if (personResult.get(0).get("id").toString() == null) {
					tmdbData.add("No ID Found");
				} else {
					tmdbData.add(personResult.get(0).get("id").toString());
				}
				List<Map<String, Object>> moviesResult = (ArrayList<Map<String, Object>>) personResult.get(0).get("known_for");

				if (moviesResult == null) {
					tmdbData.add("No Image");
				} else {
					for (int i = 0; i < 3; i++) {
						tmdbData.add("http://image.tmdb.org/t/p/w300"
								+ moviesResult.get(i).get("poster_path")
										.toString());
					}
				}
			}

		} catch (Exception e) {
			tmdbData.add("No ID Found");
			tmdbData.add("No Image");
			tmdbData.add("No Image");
			tmdbData.add("No Image");
			System.out.println(e.getMessage().toString()
					+ "Error in get TMDBID service");
		}
		return tmdbData;
	}

	/*
	 *  This function get the DOB and poster of writers, directors and actors when tmdbId is passed as input parameter.
	 */
	private List<String> getDOBInfo(String tmdbId) {
		List<String> tmdbDOBData = new ArrayList<String>();
		tmdbDOBData.clear();
		try {
			InputStream input = new URL("http://api.themoviedb.org/3/person/"
					+ tmdbId + "?api_key=3eaf57ed7c6daae4f7ef9c460134ac0f")
					.openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));

			if (response == null) {
				System.out.println("Response is null!!");
				tmdbDOBData.add("NO DOB DATA");
				tmdbDOBData.add("No Popularity Data Available");
				tmdbDOBData.add("No Place of birth mention");
			} else {

				if (response.get("birthday").toString() == null) {
					tmdbDOBData.add("NO DOB DATA");
				} else {
					tmdbDOBData.add(response.get("birthday").toString());
				}
				if (response.get("popularity").toString() == null) {
					tmdbDOBData.add("No Popularity Data Available");
				} else {
					tmdbDOBData.add(response.get("popularity").toString());
				}
				if ((response.get("place_of_birth").toString()) == null) {
					tmdbDOBData.add("No Place of birth mention");
				} else {
					tmdbDOBData.add(response.get("place_of_birth").toString());
				}

			}

		} catch (Exception e) {
			tmdbDOBData.add("NO DOB DATA");
			tmdbDOBData.add("No Popularity Data Available");
			tmdbDOBData.add("No Place of birth mention");
			System.out.println(e.getMessage().toString()
					+ "Error in get TMDBID date of birth service");
		}

		return tmdbDOBData;
	}
	
	/*
	 * 	This function is used to read the JSON response.
	 */
	public static Map<String, Object> toMapObject(String data) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(data,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (Exception ex) {
			System.err
					.println("cannot convet to map<String, Object> : " + data);
			System.err.println(ex.getMessage());
		}

		return map;
	}
	
	/*
	 * 	This function is used to remove white-spaces and special characters.
	 */
	public static String toTrim(String name) {
		name = name.replace(" ", "");
		System.out.println(name.indexOf("("));
		if (name.contains("(")) {
			name = name.substring(0, name.indexOf("("));
		}
		if (name.contains(")")) {
			name = name.replace(")", "");
		}
		System.out.println(name);
		return name;
	}
	
	/*
	 * 	This is small lookup table for IMDB genre types.
	 */
	public enum GenreList {
		Action(28), Adventure(12), Animation(16), Comedy(35), Crime(80), Documentary(
				99), Drama(18), Family(10751), Fantasy(14), Foreign(10769), History(
				36), Horror(27), Music(10402), Mystery(9648), Romance(10749), SciFi(
				878), TVMovie(10770), Thriller(53), War(10752), Western(37);

		private int value;

		private GenreList(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/*
	 * 	This function return's the genre code when genre type is passed as input parameter.
	 */
	public static String getGenreCode(String genreName) {
		int result = 0;
		genreName = genreName.replace(" ", "");
		genreName = genreName.replace("-", "");
		switch (genreName) {
		case "Action":
			result = GenreList.Action.value;
			break;
		case "Adventure":
			result = GenreList.Adventure.value;
			break;
		case "Animation":
			result = GenreList.Animation.value;
			break;
		case "Comedy":
			result = GenreList.Comedy.value;
			break;
		case "Crime":
			result = GenreList.Crime.value;
			break;
		case "Documentary":
			result = GenreList.Documentary.value;
			break;
		case "Drama":
			result = GenreList.Drama.value;
			break;
		case "Family":
			result = GenreList.Family.value;
			break;
		case "Fantasy":
			result = GenreList.Fantasy.value;
			break;
		case "Foreign":
			result = GenreList.Foreign.value;
			break;
		case "History":
			result = GenreList.History.value;
			break;
		case "Horror":
			result = GenreList.Horror.value;
			break;
		case "Music":
			result = GenreList.Music.value;
			break;
		case "Mystery":
			result = GenreList.Mystery.value;
			break;
		case "Romance":
			result = GenreList.Romance.value;
			break;
		case "SciFi":
			result = GenreList.SciFi.value;
			break;
		case "RealityTV":
			result = GenreList.TVMovie.value;
			break;
		case "Thriller":
			result = GenreList.Thriller.value;
			break;
		case "War":
			result = GenreList.War.value;
			break;
		case "Western":
			result = GenreList.Western.value;
			break;
		default:
			result=0;
			break;
		}
		return String.valueOf(result);
	}
	
	private static double getSimilarity(Mat queryDescriptor, Mat trainDescriptor) {
		MatOfDMatch matches=new MatOfDMatch();
		DescriptorMatcher	descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		descriptorMatcher.match(queryDescriptor, trainDescriptor,matches);
		List<DMatch> matchesList = matches.toList();

		Double max_dist = 0.0;
		Double min_dist = 100.0;

		for (int j = 0; j < queryDescriptor.rows(); j++) {
			Double dist = (double) matchesList.get(j).distance;
			if (dist < min_dist)
				min_dist = dist;
			if (dist > max_dist)
				max_dist = dist;
		}

		LinkedList<DMatch> good_matches = new LinkedList<>();
		double goodMatchesSum = 0.0;

		// good match = distance > 2*min_distance ==> put them in a list
		for (int k = 0; k < queryDescriptor.rows(); k++) {
			if (matchesList.get(k).distance < Math.max(2 * min_dist,
					0.02)) {
				good_matches.addLast(matchesList.get(k));
				goodMatchesSum += matchesList.get(k).distance;
			}
		}

		double simIndex = (double) goodMatchesSum
				/ (double) good_matches.size();
		
		return simIndex;
	}
	
	public  double getMaxValue(SimilarityIndex[] array){  
	    double maxValue = array[0].getIndex();  
	    
	    for(int i=1;i<best.length;i++){  
	    
	    	if(best[i].getIndex() > maxValue){  
	    		maxValue =array[i].getIndex();  	
	    	}  
	    }  
	   return maxValue;  
			}
	
	private  int getMaxPosition(double maxValue) {
		   
		int position=-1;
	    for(int i=0;i<best.length;i++){  
	    
	    	if(best[i].getIndex()== maxValue){  
	    		position=i;
	    	}  
	    }  
	   return position;  
	}
	
	
	
}
